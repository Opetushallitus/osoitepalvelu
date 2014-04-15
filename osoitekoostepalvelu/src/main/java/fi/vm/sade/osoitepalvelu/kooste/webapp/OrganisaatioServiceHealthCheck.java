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

package fi.vm.sade.osoitepalvelu.kooste.webapp;

import fi.vm.sade.generic.healthcheck.HealthChecker;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import org.apache.camel.util.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 3:05 PM
 */
@Component
@HealthCheckerName("organisaatio-serivce-online")
public class OrganisaatioServiceHealthCheck implements HealthChecker {

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Value("${root.organisaatio.oid}")
    private String rootOrganisaatioOid;

    @Override
    public Object checkHealth() throws Throwable {
        final StopWatch watch  =  new StopWatch();
        OrganisaatioDetailsDto details =  organisaatioServiceRoute.getdOrganisaatioByOid(rootOrganisaatioOid,
                new DefaultCamelRequestContext());
        final long resultTook  =  watch.stop();
        return new LinkedHashMap() { {
            put("status", "OK");
            put("response-time", ""  +  resultTook  +  " ms");
        } };
    }
}
