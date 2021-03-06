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
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KayttooikesuryhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.AuthenticationServiceRoute;

import org.apache.camel.util.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 4:42 PM
 */
//@Component // may require user to be logged in -> floods logs if healthcek polled without authentication
@HealthCheckerName("authentication-service-online")
public class AuthenticationServiceHealthCheck implements HealthChecker {
    @Autowired
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Override
    public Object checkHealth() throws Throwable {
        final StopWatch watch  =  new StopWatch();
        final List<KayttooikesuryhmaDto> kayttoikeusryhmas  =  authenticationServiceRoute.findKayttooikeusryhmas(
                new DefaultCamelRequestContext());
        final long resultTook  =  watch.stop();
        return new LinkedHashMap<Object, Object>() { 
            private static final long serialVersionUID = -8706812076630006444L;
        {
            put("status", "OK");
            put("response-time", ""  +  resultTook  +  " ms");
            put("kayttoikeusryhmas-count", kayttoikeusryhmas.size());
        } };
    }
}
