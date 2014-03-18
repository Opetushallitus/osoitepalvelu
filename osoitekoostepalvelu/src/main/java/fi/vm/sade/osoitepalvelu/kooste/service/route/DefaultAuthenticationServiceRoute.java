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

package fi.vm.sade.osoitepalvelu.kooste.service.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KayttooikesuryhmaDto;
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
public class DefaultAuthenticationServiceRoute extends AbstractJsonToDtoRouteBuilder
        implements AuthenticationServiceRoute {
    private String ROUTE_KAYTTOOIKESURYHMAS = "direct:findKayttoikeusryhmas";
    private String ROUTE_HENKILOS = "direct:henkilos";

    @Value("${authenticationService.kayttoikeusryhma.rest.url}")
    private String authenticationServiceKayttooikeusryhmasRestUrl;

    @Value("${authenticationService.henkilo.rest.url}")
    private String authenticationServiceHenkiloServiceRestUrl;

    @Value("${cas.service.authentication-service}")
    private String authenticationServiceCasServiceUrl;


    @Override
    public void configure() throws Exception {
        headers(
                casByAuthenticatedUser(
                        from(ROUTE_KAYTTOOIKESURYHMAS),
                        authenticationServiceCasServiceUrl
                ),
                headers().get()
        )
        .to(trim(authenticationServiceKayttooikeusryhmasRestUrl))
        .process(jsonToDto(new TypeReference<List<KayttooikesuryhmaDto>>() {}));

        headers(
                casByAuthenticatedUser(
                        from(ROUTE_HENKILOS),
                        authenticationServiceCasServiceUrl
                ),
                headers()
                    .get().path("/byOoids")
                    .query("ht=VIRKAILIJA&ooids=${in.headers.ooids}")
        )
        .to(trim(authenticationServiceHenkiloServiceRestUrl))
        .process(jsonToDto(new TypeReference<List<HenkiloDto>>() {}));
    }

    @Override
    public List<KayttooikesuryhmaDto> findKayttooikeusryhmas() {
        return getCamelTemplate().requestBody(ROUTE_KAYTTOOIKESURYHMAS, "", List.class);
    }

    // TODO: TEST ME, does not exists in luokka yet
    @Override
    public List<HenkiloDto> findHenkilosByOrganisaatioOids(List<String> ooids) {
        return getCamelTemplate().requestBodyAndHeaders(ROUTE_HENKILOS, "",
                headerValues()
                        .add("ooids", StringHelper.join(",", ooids.toArray(new String[0])))
                        .map(), List.class);
    }
}