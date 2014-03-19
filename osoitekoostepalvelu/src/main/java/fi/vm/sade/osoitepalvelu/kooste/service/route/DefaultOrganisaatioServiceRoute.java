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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 1:54 PM
 */
@Service
public class DefaultOrganisaatioServiceRoute extends AbstractJsonToDtoRouteBuilder
            implements  OrganisaatioServiceRoute {
    private static final String ORGANSIAATIOHAKU_ENDPOINT = "direct:organisaatioYhteystietohakuV2";
    public static final String YHTEYSTIEDOT_PATH = "/v2/yhteystiedot/hae";

    @Value("${organisaatioService.rest.url}")
    private String organisaatioServiceRestUrl;

    @Value("${cas.service.organisaatio-service}")
    private String organisaatioServiceCasServiceUrl;

    @Override
    public void configure() throws Exception {
        headers(
                from(ORGANSIAATIOHAKU_ENDPOINT),
                headers()
                    .post()
                    .jsonRequstBody()
                    .path(YHTEYSTIEDOT_PATH)
                    .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .to(trim(organisaatioServiceRestUrl))
        .process(jsonToDto(new TypeReference<List<OrganisaatioYhteystietoHakuResultDto>>() {}));
    }

    @Override
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria) {
        return getCamelTemplate().requestBody(ORGANSIAATIOHAKU_ENDPOINT, criteria, List.class);
    }
}
