/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.scheduled;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasDisabledCasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import org.apache.camel.RuntimeCamelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Keep organisaatios in cache.
 *
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:20 AM
 */
@Service
public class ScheduledOrganisaatioCacheTask extends AbstractService {
    // While the timeout for Camel requests is 15s with 2 retries, this should allow
    // Organisaatio service to have over 2 minutes of downtime or couple of 500 errors at any given point
    // (while the overall process would take about half an hour):
    public static final int MAX_TRIES = 3;
    public static final int WAIT_BEFORE_RETRY_MILLIS = 3000;

    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Value("${web.url.cas}")
    protected String casService;

    // Every working day night at 3 AM
    @Scheduled(cron = "0 0 3 * * MON-FRI")
    public void refreshOrganisaatioCache() {
        logger.info("BEGIN SCHEDULED refreshOrganisaatioCache.");

        // No CAS here (not needed for reading organisaatio service):
        final CamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(
                new CasDisabledCasTicketProvider()));

        List<String> oids = retryOnCamelError(new Callable<List<String>>() {
            public List<String> call() throws Exception {
                return organisaatioServiceRoute.findAllOrganisaatioOids(context);
            }
        }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);

        logger.info("Found {} organisaatios to refresh.", oids.size());
        try {
            int i = 0;
            for (final String oid : oids) {
                ++i;
                // This will purge the possibly cached organisaatio from both method level memory based EH cache and
                // and the underlying MongoDB cache:
                organisaatioService.purgeOrganisaatioByOidCache(oid);
                // ...and renew the cache:
                retryOnCamelError(new Callable<OrganisaatioDetailsDto>() {
                    public OrganisaatioDetailsDto call() throws Exception {
                        return organisaatioService.getdOrganisaatioByOid(oid, context);
                    }
                }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);

                logger.info("Updated organisaatio {} (Total: {} / {})", new Object[]{oid, i, oids.size()});
            }
        } finally {
            logger.info("UPDATING y-tunnus details...");
            organisaatioService.updateOrganisaatioYtunnusDetails(context);
            logger.info("DONE UPDATING y-tunnus details");
        }

        logger.info("END SCHEDULED refreshOrganisaatioCache.");
    }

    /**
     * Does not purge fresh cache records but ensures that all organisaatios are cached.
     */
    public void ensureOrganisaatioCacheFresh() {
        logger.info("BEGIN SCHEDULED ensureOrganisaatioCacheFresh.");

        // No CAS here (not needed for reading organisaatio service):
        final CamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(
                new CasDisabledCasTicketProvider()));

        List<String> oids = retryOnCamelError(new Callable<List<String>>() {
            public List<String> call() throws Exception {
                return organisaatioServiceRoute.findAllOrganisaatioOids(context);
            }
        }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);

        logger.info("Found {} organisaatios to ensure cache.", oids.size());
        boolean infoUpdated = false;
        try {
            int i = 0;
            for (final String oid : oids) {
                ++i;
                // ...and renew the cache:
                OrganisaatioDetailsDto details = retryOnCamelError(new Callable<OrganisaatioDetailsDto>() {
                    public OrganisaatioDetailsDto call() throws Exception {
                        return organisaatioService.getdOrganisaatioByOid(oid, context);
                    }
                }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);
                if (details.isFresh()) {
                    infoUpdated = true;
                    logger.info("Updated organisaatio {} (Total: {} / {})", new Object[]{oid, i, oids.size()});
                } else if(i % 1000 == 0) {
                    logger.info("Organisaatio ensure data fresh task status: {} / {}", new Object[]{i, oids.size()});
                }
            }
        } finally {
            logger.info("UPDATING y-tunnus details...");
            organisaatioService.updateOrganisaatioYtunnusDetails(context);
            logger.info("DONE UPDATING y-tunnus details");
        }

        logger.info("END SCHEDULED ensureOrganisaatioCacheFresh.");
    }

    /**
     * Retries implemented here, not in the actual Camel routes, because we don't want them to be part of every Camel
     * call (calls made by the user). In this scheduled task, however, it is more important to continue the process
     * after possible downtime.
     *
     * @param task to call
     * @param maxRetries max retries
     * @param retryWaitTimeMillis millis to wait before retryOnCamelError
     * @param <T> type to return
     * @return the result of the successful call
     */
    protected<T> T retryOnCamelError(Callable<T> task, int maxRetries, long retryWaitTimeMillis) {
        RuntimeCamelException originalError = null;
        for (int j = 0; j < maxRetries+1; ++j) {
            try {
                return task.call();
            } catch(Exception e) {
                if (!(e instanceof RuntimeCamelException)) {
                    // For every other error, fail:
                    throw new IllegalStateException("Scheduled task error:"+e.getMessage(), e);
                }
                if (originalError == null) {
                    originalError =(RuntimeCamelException) e;
                }
                logger.warn("Error fetching data: " + e.getMessage(), e);
                try {
                    Thread.sleep(retryWaitTimeMillis);
                } catch(InterruptedException er) {}
                if (j < maxRetries) {
                    logger.info("Retrying...");
                }
            }
        }
        logger.error("SCHEDULED task exhausted after " + maxRetries
                + " retries. Original exception: " + originalError.getMessage(), originalError);
        throw originalError;
    }

}
