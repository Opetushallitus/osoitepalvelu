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
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.AituService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Service
public class ScheduledAituDataFetchTask extends AbstractService {

    @Autowired
    private AituService aituService;

    @Value("${web.url.cas.aitu:}")
    private String aituCasService;
    @Value("${cas.service.aitu-service:}")
    private String aituServiceCasServiceUrl;
    @Value("${osoitepalvelu.app.username.to.aituservice:}")
    private String casAituServiceUsername;
    @Value("${osoitepalvelu.app.password.to.aituservice:}")
    private String casAituServicePassword;

    // Every night at 4 AM
    @Scheduled(cron = "0 14 16 * * MON-FRI")
    public void refreshAituData() {
        logger.info("BEGIN SCHEDULED refreshAituData.");

        CasTicketProvider casProvider;
        if (casAituServiceUsername != null && casAituServiceUsername.trim().length() > 0) {
            logger.info("Using CAS with username {}", casAituServiceUsername);
            casProvider = new UsernamePasswordCasClientTicketProvider(aituCasService, casAituServiceUsername,
                    casAituServicePassword, false);
        } else {
            logger.info("CAS disabled.");
            casProvider = new CasDisabledCasTicketProvider();
        }
        CamelRequestContext context = new DefaultCamelRequestContext(new ProviderOverriddenCasTicketCache(casProvider));
        aituService.refreshData(context);

        logger.info("END SCHEDULED refreshAituData.");
    }
}
