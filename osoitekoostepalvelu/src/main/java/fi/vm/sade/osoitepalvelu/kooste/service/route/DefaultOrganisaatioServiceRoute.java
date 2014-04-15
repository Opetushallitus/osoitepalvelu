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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 1:54 PM
 */
@Service
public class DefaultOrganisaatioServiceRoute extends AbstractJsonToDtoRouteBuilder
            implements  OrganisaatioServiceRoute {
    private static final String SERVICE_CALL_ORGANSIAATIO_POSTFIX = ".OrgansiaatioServiceCall";

    private static final String ORGANSIAATIO_OID_LIST_ENDPOINT  =  "direct:organisaatioOidList";
    private static final String ORGANISAATIO_OIDS_PATH  = "/";

    private static final String ORGANSIAATIOHAKU_ENDPOINT  =  "direct:organisaatioYhteystietohakuV2";

    private static final String YHTEYSTIEDOT_PATH  =  "/v2/yhteystiedot/hae";
    private static final String SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT  =  "direct:singleOrganisaatioByOid";
    private static final String SINGLE_ORGANISAATIO_PATH  =  "/${in.headers.oid}";

    private static final String ORGANISAATIO_HIERARCHY_ENDPOINT = "direct:organisaatioHierarchyHaku";
    private static final String ORGANISAATIO_HIERARCHY_PATH = "/hae";
    private static final String ORGANISAATIO_HIERACHY_TYYPPI_PARAM = "organisaatiotyyppi";
    private static final String ORGANISAATIO_HIERACHY_VAIN_AKTIIVISET_PARAM = "vainAktiiviset";


    @Value("${organisaatioService.rest.url}")
    private String organisaatioServiceRestUrl;

    @Value("${cas.service.organisaatio-service}")
    private String organisaatioServiceCasServiceUrl;

    @Override
    public void configure() throws Exception {
        buildOrganisaatioOidList();
        buildOrganisaatioHaku();
        buildSingleOrganisaatioTiedot();
        buildOrganisaatioHierarchyHaku();
    }

    protected void buildOrganisaatioOidList() {
        Debugger organisaatioCallInOutDebug  =  debug(ORGANSIAATIO_OID_LIST_ENDPOINT+SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(ORGANSIAATIO_OID_LIST_ENDPOINT),
                headers()
                        .get()
                        .path(ORGANISAATIO_OIDS_PATH)
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(organisaatioServiceRestUrl))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<String>>() {}));
    }

    protected void buildOrganisaatioHaku() {
        Debugger organisaatioCallInOutDebug  =  debug(ORGANSIAATIOHAKU_ENDPOINT + SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(ORGANSIAATIOHAKU_ENDPOINT),
                headers()
                        .post()
                        .jsonRequstBody()
                        .path(YHTEYSTIEDOT_PATH)
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(organisaatioServiceRestUrl, 10L*60L*1000L)) // wait for 10 minutes maximum
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<OrganisaatioYhteystietoHakuResultDto>>() {}));
    }

    protected void buildSingleOrganisaatioTiedot() {
        Debugger organisaatioCallInOutDebug  =  debug(SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT
                +SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT),
                headers()
                        .get()
                        .path(SINGLE_ORGANISAATIO_PATH)
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(organisaatioServiceRestUrl))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<OrganisaatioDetailsDto>() {}));
    }

    protected void buildOrganisaatioHierarchyHaku() {
        Debugger authenticationCallInOutDebug  =  debug(ORGANISAATIO_HIERARCHY_ENDPOINT
                +SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(ORGANISAATIO_HIERARCHY_ENDPOINT),
                headers()
                        .get()
                        .path(ORGANISAATIO_HIERARCHY_PATH)
                            .param(ORGANISAATIO_HIERACHY_TYYPPI_PARAM).optional().valueFromBody().toQuery()
                            .param(ORGANISAATIO_HIERACHY_VAIN_AKTIIVISET_PARAM).value(true).toQuery()
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(organisaatioServiceRestUrl))
        .process(authenticationCallInOutDebug)
        .process(jsonToDto(new TypeReference<OrganisaatioHierarchyResultsDto>() {}));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> findAllOrganisaatioOids(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ORGANSIAATIO_OID_LIST_ENDPOINT,
                "", new HashMap<String, Object>(), requestContext, List.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria,
            CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ORGANSIAATIOHAKU_ENDPOINT,
                criteria, new HashMap<String, Object>(), requestContext, List.class);
    }

    @Override
    public OrganisaatioDetailsDto getdOrganisaatioByOid(String oid, CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(),
                SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT, "",
                headerValues()
                    .add("oid", oid)
                .map(), requestContext, OrganisaatioDetailsDto.class);

    }

    @Override
    public OrganisaatioHierarchyResultsDto findOrganisaatioHierachyByTyyppi(String tyyppi,
                                                                            CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(),
                ORGANISAATIO_HIERARCHY_ENDPOINT, tyyppi,
                headerValues().map(), requestContext, OrganisaatioHierarchyResultsDto.class);
    }
}
