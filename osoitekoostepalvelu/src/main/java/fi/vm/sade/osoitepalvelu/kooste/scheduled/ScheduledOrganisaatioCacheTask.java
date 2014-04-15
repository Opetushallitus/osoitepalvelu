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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Keep organisaatios in cache.
 *
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:20 AM
 */
@Service
public class ScheduledOrganisaatioCacheTask extends AbstractService {
    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Value("${web.url.cas}")
    protected String casService;

    // Every night at 3 AM
    @Scheduled(cron = "0 0 3 * * MON-FRI")
    public void refreshOrganisaatioCache() {
        logger.info("BEGIN SCHEDULED refreshOrganisaatioCache.");

        // Since this is a system call and we don't have access to SpringSecurity's context, override the usual
        // CasTicketCache by one that falls back to system user account CasTicketProvider unless there was a cache hit:
        CamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(
                new CasDisabledCasTicketProvider()));

        List<String> oids = organisaatioServiceRoute.findAllOrganisaatioOids(context);
        logger.info("Found {} organisaatios to refresh.", oids.size());
        int i = 0;
        for (String oid : oids) {
            ++i;
            // This will purge the possibly cached organisaatio from both method level memory based EH cache and
            // and the underlying MongoDB cache:
            organisaatioService.purgeOrganisaatioByOidCache(oid);
            // ...and renew the cache:
            organisaatioService.getdOrganisaatioByOid(oid, context);
            logger.info("Updated organisaatio {} (Total: {} / {})", new Object[]{oid, i, oids.size()});
        }

        logger.info("END SCHEDULED refreshOrganisaatioCache.");
    }

}
