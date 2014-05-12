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

import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User: ratamaa
 * Date: 5/12/14
 * Time: 1:48 PM
 */
@Component("startupTasks")
public class ApplicationStartupTasks extends AbstractService {

    @Autowired
    private ScheduledOrganisaatioCacheTask organisaatioCacheTask;

    @Autowired
    private ScheduledAituDataFetchTask aituDataFetchTask;

    // Only once (can not use ContextRefreshedEvent since it would block the startup)
    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void afterStartup() {
        logger.info("STATRUP tasks BEGIN");
        try {
            organisaatioCacheTask.ensureOrganisaatioCacheFresh();
        } catch(Throwable e) {
            logger.error("Failed to ensure Organisaatio cache state: " + e.getMessage());
        }
        try {
            aituDataFetchTask.refreshAituData();
        } catch(Throwable e) {
            logger.error("Failed to fetch AITU data: " + e.getMessage());
        }
        logger.info("STATRUP tasks END");
    }
}
