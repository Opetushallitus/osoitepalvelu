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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.JacksonJsonProcessor;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KayttooikesuryhmaDto;
import org.apache.camel.Exchange;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 2:56 PM
 */
@Component
public class AuthenticationServiceRoute extends AbstractJsonToDtoRouteBuilder {
    private String ROUTE_KAYTTOOIKESURYHMAS = "direct:findKayttoikeusryhmas";

    @Value("${authenticationService.kayttoikeusryhma.rest.url}")
    private String authenticationServiceKayttooikeusryhmasUri;

    //@Value("${cas.service.authentication-service}")
    private String authenticationServiceCasService = "https://itest-virkailija.oph.ware.fi/authentication-service/j_spring_cas_security_check";

    @Override
    public void configure() throws Exception {
        casAuthenticated(from(ROUTE_KAYTTOOIKESURYHMAS), authenticationServiceCasService)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to(trim(authenticationServiceKayttooikeusryhmasUri))
                .process(new JacksonJsonProcessor(mapperProvider, new TypeReference<List<KayttooikesuryhmaDto>>() {}));
    }

    public List<KayttooikesuryhmaDto> findKayttooikeusryhmas() {
        return getCamelTemplate().requestBody(ROUTE_KAYTTOOIKESURYHMAS, "", List.class);
    }
}