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
import fi.vm.sade.osoitepalvelu.kooste.common.util.DateHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.organisaatio.OrganisaatioRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.OivaRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import org.apache.camel.RuntimeCamelException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Keep organisaatios in cache.
 */
@Service
public class ScheduledOrganisaatioCacheTask extends AbstractService {
    // While the timeout for Camel requests is 15s with 2 retries, this should allow
    // Organisaatio service to have over 2 minutes of downtime or couple of 500 errors at any given point
    // (while the overall process would take about half an hour):
    public static final int MAX_TRIES = 3;
    public static final int WAIT_BEFORE_RETRY_MILLIS = 3000;
    private static final int LOGGIN_INTERVAL = 1000;

    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired
    private OivaRoute oivaRoute;

    @Autowired
    private OrganisaatioRepository organisaatioRepository;

    @Value("${web.url.cas}")
    protected String casService;

    @Value("${organisaatio.cache.valid.from:}")
    private String cacheValidFrom;


    // Every working day night at 3 AM
    @Scheduled(cron = "0 0 3 * * MON-FRI")
    public void refreshOrganisaatioCache() {
        logger.info("BEGIN SCHEDULED refreshOrganisaatioCache.");
        showCacheState();

        // No CAS here (not needed for reading organisaatio service):
        final CamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(
                new CasDisabledCasTicketProvider()));

        List<String> oids = retryOnCamelError(new Callable<List<String>>() {
            public List<String> call() {
                return organisaatioServiceRoute.findAllOrganisaatioOids(context);
            }
        }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);
        removeCachedDeletedOrganisaatios(oids);

        logger.info("Found {} organisaatios to refresh.", oids.size());
        int i = 0;
        try {
            for (final String oid : oids) {
                ++i;
                // This will purge the possibly cached organisaatio from both method level memory based EH cache and
                // and the underlying MongoDB cache:
                organisaatioService.purgeOrganisaatioByOidCache(oid);
                // ...and renew the cache:
                retryOnCamelError(new Callable<OrganisaatioDetailsDto>() {
                    public OrganisaatioDetailsDto call() {
                        return organisaatioService.getdOrganisaatioByOid(oid, context);
                    }
                }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);

                logger.debug("Updated organisaatio {} (Total: {} / {})", new Object[]{oid, i, oids.size()});
            }
        } finally {
            logger.debug("Updated count {}", i);
        }

        updateFromOiva(context);

        logger.info("END SCHEDULED refreshOrganisaatioCache.");
    }

    private void removeCachedDeletedOrganisaatios(List<String> oids) {
        logger.info("REMOVING obsolete organisaatios from cache.");
        List<String> cachedObsoleteOids = organisaatioService.findAllOidsOfCachedOrganisaatios();
        cachedObsoleteOids.removeAll(oids);
        for (String oid : cachedObsoleteOids) {
            organisaatioService.purgeOrganisaatioByOidCache(oid);
        }
        logger.info("REMOVED {} obsolete organsiaatios from cache.", cachedObsoleteOids.size());
    }

    /**
     * Does not purge fresh cache records but ensures that all organisaatios are cached.
     */
    public void ensureOrganisaatioCacheFresh() {
        logger.info("BEGIN SCHEDULED ensureOrganisaatioCacheFresh.");
        showCacheState();

        LocalDate cacheInvalidBefore = null;
        if (cacheValidFrom != null && cacheValidFrom.length() > 0) {
            cacheInvalidBefore = LocalDate.parse(cacheValidFrom);
        }

        // No CAS here (not needed for reading organisaatio service):
        final DefaultCamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(
                new CasDisabledCasTicketProvider()));
        if (cacheInvalidBefore != null && new DateTime().compareTo(cacheInvalidBefore.toDateTimeAtStartOfDay()) < 0) {
            context.setOverriddenTime(cacheInvalidBefore.toDateTimeAtStartOfDay());
        }

        List<String> oids = retryOnCamelError(new Callable<List<String>>() {
            public List<String> call() {
                return organisaatioServiceRoute.findAllOrganisaatioOids(context);
            }
        }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);
        removeCachedDeletedOrganisaatios(oids);

        logger.info("Found {} organisaatios to ensure cache.", oids.size());
        boolean infoUpdated = false;
        int i = 0;
        try {
            for (final String oid : oids) {
                ++i;
                // ...and renew the cache:
                long rc = context.getRequestCount();
                retryOnCamelError(new Callable<OrganisaatioDetailsDto>() {
                    public OrganisaatioDetailsDto call() {
                        return organisaatioService.getdOrganisaatioByOid(oid, context);
                    }
                }, MAX_TRIES, WAIT_BEFORE_RETRY_MILLIS);

                if (rc != context.getRequestCount()) {
                    infoUpdated = true;
                    logger.debug("Updated organisaatio {} (Total: {} / {})", new Object[]{oid, i, oids.size()});
                } else if(i % LOGGIN_INTERVAL == 0) {
                    logger.info("Organisaatio ensure data fresh task status: {} / {}", new Object[]{i, oids.size()});
                }
            }
        } finally {
            logger.debug("Updated count {}", i);
        }

        if (infoUpdated) {
            updateFromOiva(context);
        }

        logger.info("END SCHEDULED ensureOrganisaatioCacheFresh.");
    }

    private void showCacheState() {
        DateTime oldestEntry = organisaatioRepository.findOldestCachedEntry();
        if (oldestEntry != null) {
            logger.info("Oldest cache entry: {}", oldestEntry);
        } else {
            logger.info("No cache entries found.");
        }
    }

    private void updateFromOiva(CamelRequestContext context) {
        try {
            logger.info("Updating from Oiva.");
            LocalDate date = LocalDate.now();
            oivaRoute.listKoulutuslupa(context).stream()
                    .filter(koulutuslupa -> DateHelper.isBetweenInclusive(date, koulutuslupa.getAlkupvm(), koulutuslupa.getLoppupvm()))
                    .forEach(koulutuslupa -> {
                        String jarjestajaYtunnus = koulutuslupa.getJarjestajaYtunnus();
                        Optional<OrganisaatioDetails> organisaatioOptional = organisaatioRepository.findByYtunnus(jarjestajaYtunnus);
                        if (organisaatioOptional.isPresent()) {
                            OrganisaatioDetails organisaatioDetails = organisaatioOptional.get();
                            organisaatioDetails.setKoulutusluvat(koulutuslupa.getKoulutukset());
                            organisaatioRepository.save(organisaatioDetails);
                        } else {
                            logger.info("Cannot find organisaatio with y-tunnus {}", jarjestajaYtunnus);
                        }
                    });
            logger.info("Updating from Oiva finished.");
        } catch (Exception e) {
            logger.error("Updating from Oiva failed.", e);
        }
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
                } catch(InterruptedException er) {
                    logger.warn("Error while sleeping: " + er.getMessage(), er);
                }
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
