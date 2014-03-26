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
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.*;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 2:56 PM
 */
@Component
public class DefaultAuthenticationServiceRoute extends AbstractJsonToDtoRouteBuilder
        implements AuthenticationServiceRoute {
    private static final String SERVICE_CALL_POSTFIX = ".AuthenticationServiceCall";

    private static final String ROUTE_KAYTTOOIKESURYHMAS  =  "direct:findKayttoikeusryhmas";

    private static final String ROUTE_HENKILOS  =  "direct:henkiloList";
    private static final String HENKILOS_BY_OOIDS_PATH = "/byOoids";
    private static final String HENKILOS_OOIDS_PARAM_NAME = "ooids";
    private static final String HT_VIRKAILIJA = "VIRKAILIJA";

    private static final String ROUTE_HENKILO = "direct:henkilo";
    private static final String HENKILO_PATH = "/${in.body}";

    private static final String ROUTE_ORGANISAATIOHENKILOS  =  "direct:organisaatioHenkilos";
    private static final String ORGANISAATIOHENKILOS_PATH  =  "/${in.body}/organisaatiohenkilo";

    private static final String ROUTE_MY_INFORMATION  =  "direct:myInformation";
    private static final String MY_INFORMATION_PATH  =  "/me";

    @Value("${authenticationService.kayttoikeusryhma.rest.url}")
    private String authenticationServiceKayttooikeusryhmasRestUrl;

    @Value("${authenticationService.henkilo.rest.url}")
    private String authenticationServiceHenkiloServiceRestUrl;

    @Value("${cas.service.authentication-service}")
    private String authenticationServiceCasServiceUrl;


    @Override
    public void configure() throws Exception {
        buildKayttoOikeusryhmas();
        buildHenkilo();
        buildOrganisaatioHenkilos();
        buildHenkiloList();
        buildMyInformation();
    }

    protected void buildHenkilo() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_HENKILO + SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILO),
            headers()
                .get().path(HENKILO_PATH)
                .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
        )
        .process(authenticationCallInOutDebug)
        .to(trim(authenticationServiceHenkiloServiceRestUrl))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<HenkiloDetailsDto>() {}));
    }

    protected void buildOrganisaatioHenkilos() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_ORGANISAATIOHENKILOS + SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_ORGANISAATIOHENKILOS),
            headers()
                .get().path(ORGANISAATIOHENKILOS_PATH)
                .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
        )
        .process(authenticationCallInOutDebug)
        .to(trim(authenticationServiceHenkiloServiceRestUrl))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<OrganisaatioHenkiloDto>>() { }));
    }

    protected void buildHenkiloList() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_HENKILOS + SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILOS),
            headers()
                .get().path(HENKILOS_BY_OOIDS_PATH)
                        .queryParam("ht", HT_VIRKAILIJA)
                        .queryArrayParam(HENKILOS_OOIDS_PARAM_NAME)
                .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
        )
        .process(authenticationCallInOutDebug)
        .to(trim(authenticationServiceHenkiloServiceRestUrl))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<HenkiloListResultDto>>() { }));
    }

    protected void buildKayttoOikeusryhmas() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_KAYTTOOIKESURYHMAS + SERVICE_CALL_POSTFIX);
        headers(
                from(ROUTE_KAYTTOOIKESURYHMAS),
                headers()
                        .get()
                        .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
      )
        .process(authenticationCallInOutDebug)
        .to(trim(authenticationServiceKayttooikeusryhmasRestUrl))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<KayttooikesuryhmaDto>>() { }));
    }

    protected void buildMyInformation() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_MY_INFORMATION + SERVICE_CALL_POSTFIX);
        headers(
                from(ROUTE_MY_INFORMATION),
                headers()
                        .get().path(MY_INFORMATION_PATH)
                        .casAuthenticationByAuthenticatedUser(casService)
      )
        .process(authenticationCallInOutDebug)
        .to(trim(casService))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<MyInformationDto>() {}));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<KayttooikesuryhmaDto> findKayttooikeusryhmas(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(),
                ROUTE_KAYTTOOIKESURYHMAS, "", new HashMap<String, Object>(), requestContext, List.class);
    }

    @Override
    public HenkiloDetailsDto getHenkiloTiedot(String oid, CamelRequestContext requestContext) {
        HenkiloDetailsDto details = sendBodyHeadersAndProperties(getCamelTemplate(), ROUTE_HENKILO, oid,
                new HashMap<String, Object>(), requestContext, HenkiloDetailsDto.class);

        @SuppressWarnings("unchecked")
        List<OrganisaatioHenkiloDto> organisaatioHenkilos = sendBodyHeadersAndProperties(getCamelTemplate(),
                ROUTE_ORGANISAATIOHENKILOS, oid, new HashMap<String, Object>(), requestContext, List.class);
        details.setOrganisaatioHenkilos(organisaatioHenkilos);

        return details;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ROUTE_HENKILOS, "",
                headerValues()
                        .add(HENKILOS_OOIDS_PARAM_NAME, criteria.getOrganisaatioOids())
                    .map(),
                requestContext, List.class);
    }

    @Override
    public MyInformationDto getMyInformation(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ROUTE_MY_INFORMATION, "",
                new HashMap<String, Object>(), requestContext, MyInformationDto.class);
    }
}