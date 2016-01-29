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

package fi.vm.sade.osoitepalvelu.kooste.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    private static final long serialVersionUID = -5752885878113285232L;

    private static final long HENKILOLIST_TIMEOUT_MILLIS = 10L*60L*1000L;
    private static final long HENKILO_TIMEOUT_MILLIS = 30L*MILLIS_IN_SECOND;

    private static final String SERVICE_CALL_POSTFIX = ".AuthenticationServiceCall";

    private static final String ROUTE_KAYTTOOIKESURYHMAS  =  "direct:findKayttoikeusryhmas";

    private static final String ROUTE_HENKILOS  =  "direct:henkiloList";
    private static final String HENKILOS_HAKU_PATH = "/byOoids";
    private static final String HENKILOS_ORGANISAATIOOIDS_PARAM_NAME = "ooids";
    private static final String HENKILOS_KAYTTOOIKEUSRYHMAS_PARAM_NAME = "kor";
    private static final String HENKILOS_HENKILOTYYPPI_PARAM = "ht";
    private static final String HENKILOS_HENKILOTYYPPI_VIRKAILIJA = "VIRKAILIJA";
    private static final String HENKILOS_COUNT_PARAM = "count";
    private static final String HENKILOS_INDEX_PARAM = "index";

    private static final String ROUTE_HENKILO = "direct:henkilo";
    private static final String HENKILO_PATH = "/${in.body}";

    private static final String ROUTE_ORGANISAATIOHENKILOS  =  "direct:organisaatioHenkilos";
    private static final String ORGANISAATIOHENKILOS_PATH  =  "/${in.body}/organisaatiohenkilo";
    private static final int MAX_OIDS_FOR_HENKILO_HAKU = 50;

    @Value("${authenticationService.kayttoikeusryhma.rest.url}")
    private String authenticationServiceKayttooikeusryhmasRestUrl;

    @Value("${authenticationService.henkilo.rest.url}")
    private String authenticationServiceHenkiloServiceRestUrl;

    @Value("${cas.service.authentication-service}")
    private String authenticationServiceCasServiceUrl;


    @Override
    public void configure() {
        buildKayttoOikeusryhmas();
        buildHenkilo();
        buildOrganisaatioHenkilos();
        buildHenkiloList();
    }

    protected void buildHenkilo() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_HENKILO + SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILO),
            headers()
                .get().path(HENKILO_PATH)
                .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
                .retry(3)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(authenticationServiceHenkiloServiceRestUrl, HENKILO_TIMEOUT_MILLIS))
        .process(authenticationCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<HenkiloDetailsDto>() {}));
    }

    protected void buildOrganisaatioHenkilos() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_ORGANISAATIOHENKILOS + SERVICE_CALL_POSTFIX);
        headers(
                from(ROUTE_ORGANISAATIOHENKILOS),
                headers()
                        .get().path(ORGANISAATIOHENKILOS_PATH)
                        .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
                .retry(3)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(authenticationServiceHenkiloServiceRestUrl, HENKILO_TIMEOUT_MILLIS))
        .process(authenticationCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<List<OrganisaatioHenkiloDto>>() {}));
    }

    protected void buildHenkiloList() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_HENKILOS + SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILOS),
            headers()
                 // TODO: Muuttumassa POST-pyynnöksi, jotta URL:n pituus saadaan riittämään.
                 // Muuta silloin .get() -> .post() ja  .toQuery() -> .toBody()
                .get()
                .path(HENKILOS_HAKU_PATH)
                    .param(HENKILOS_HENKILOTYYPPI_PARAM)
                        .value(HENKILOS_HENKILOTYYPPI_VIRKAILIJA).toQuery()
                    .param(HENKILOS_COUNT_PARAM)
                        .value(0).toQuery()
                    .param(HENKILOS_INDEX_PARAM)
                        .value(0).toQuery()
                    .param(HENKILOS_ORGANISAATIOOIDS_PARAM_NAME)
                        .listFromHeader().toQuery()
                    .param(HENKILOS_KAYTTOOIKEUSRYHMAS_PARAM_NAME)
                        .optional().valueFromHeader().toQuery()
                .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
                .retry(3)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(authenticationServiceHenkiloServiceRestUrl, HENKILOLIST_TIMEOUT_MILLIS)) // wait for 10 minutes maximum
        .process(authenticationCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<List<HenkiloListResultDto>>() {}));
    }

    protected void buildKayttoOikeusryhmas() {
        Debugger authenticationCallInOutDebug  =  debug(ROUTE_KAYTTOOIKESURYHMAS + SERVICE_CALL_POSTFIX);
        headers(
                from(ROUTE_KAYTTOOIKESURYHMAS),
                headers()
                        .get()
                        .casAuthenticationByAuthenticatedUser(authenticationServiceCasServiceUrl)
                .retry(3)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(authenticationServiceKayttooikeusryhmasRestUrl))
        .process(authenticationCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<List<KayttooikesuryhmaDto>>() {}));
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
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        if (criteria.getOrganisaatioOids() == null || criteria.getOrganisaatioOids().isEmpty()) {
            return findByKayttoOikeusRyhmas(criteria, headerValues(), requestContext);
        }

        List<HenkiloListResultDto> results = new ArrayList<HenkiloListResultDto>();
        List<List<String>> oidChunks = CollectionHelper.split(criteria.getOrganisaatioOids(), MAX_OIDS_FOR_HENKILO_HAKU);
        for (List<String> oids : oidChunks) {
            HeaderValueBuilder header = headerValues()
                    .add(HENKILOS_ORGANISAATIOOIDS_PARAM_NAME, oids);
            results.addAll(findByKayttoOikeusRyhmas(criteria, header, requestContext));
        }

        return results;
    }

    protected List<HenkiloListResultDto> findByKayttoOikeusRyhmas(HenkiloCriteriaDto criteria, HeaderValueBuilder header,
                                                                  CamelRequestContext requestContext) {
        List<HenkiloListResultDto> results = new ArrayList<HenkiloListResultDto>();
        if (!criteria.getKayttoOikeusRayhmas().isEmpty()) {
            for (String kor : criteria.getKayttoOikeusRayhmas()) {
                @SuppressWarnings("unchecked")
                List<HenkiloListResultDto> korResults = sendBodyHeadersAndProperties(
                        getCamelTemplate(), ROUTE_HENKILOS, "", header.copy()
                        .add(HENKILOS_KAYTTOOIKEUSRYHMAS_PARAM_NAME, kor).map(),
                        requestContext, List.class);
                results.addAll(korResults);
            }
        } else {
            @SuppressWarnings("unchecked")
            List<HenkiloListResultDto> searchResults =  sendBodyHeadersAndProperties(getCamelTemplate(),
                    ROUTE_HENKILOS, "", header.map(), requestContext, List.class);
            results.addAll(searchResults);
        }
        return results;
    }
}