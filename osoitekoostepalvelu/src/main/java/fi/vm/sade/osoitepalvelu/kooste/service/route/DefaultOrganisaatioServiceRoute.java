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

import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtaisetTiedotDto;
import org.apache.camel.component.http.HttpOperationFailedException;
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
    private static final String ORGANSIAATIO_OID_LIST_ENDPOINT  =  "direct:organisaatioOidList";
    private static final String ORGANSIAATIOHAKU_ENDPOINT  =  "direct:organisaatioYhteystietohakuV2";
    private static final String YHTEYSTIEDOT_PATH  =  "/v2/yhteystiedot/hae";
    private static final String SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT  =  "direct:singleOrganisaatioByOid";
    private static final String SINGLE_ORGANISAATIO_PATH  =  "/${in.headers.oid}";
    private static final String ORGANISAATIO_OIDS_PATH  = "/";


    @Value("${organisaatioService.rest.url}")
    private String organisaatioServiceRestUrl;

    @Value("${cas.service.organisaatio-service}")
    private String organisaatioServiceCasServiceUrl;

    @Override
    public void configure() throws Exception {
        buildOrganisaatioOidList();
        buildOrganisaatioHaku();
        buildSingleOrganisaatioTiedot();
    }

    protected void buildOrganisaatioOidList() {
        Debugger organisaatioCallInOutDebug  =  debug(ORGANSIAATIO_OID_LIST_ENDPOINT  + ".OrgansiaatioServiceCall");
        headers(
                from(ORGANSIAATIO_OID_LIST_ENDPOINT),
                headers()
                        .get()
                        .path(ORGANISAATIO_OIDS_PATH)
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
        )
        .process(organisaatioCallInOutDebug)
        .to(trim(organisaatioServiceRestUrl))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<String>>() { }));
    }

    protected void buildOrganisaatioHaku() {
        Debugger organisaatioCallInOutDebug  =  debug(ORGANSIAATIOHAKU_ENDPOINT + ".OrgansiaatioServiceCall");
        headers(
                from(ORGANSIAATIOHAKU_ENDPOINT),
                headers()
                    .post()
                    .jsonRequstBody()
                    .path(YHTEYSTIEDOT_PATH)
                    .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
      )
        .process(organisaatioCallInOutDebug)
        .to(trim(organisaatioServiceRestUrl))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<List<OrganisaatioYhteystietoHakuResultDto>>() { }));
    }

    protected void buildSingleOrganisaatioTiedot() {
        Debugger organisaatioCallInOutDebug  =  debug(SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT  + ".OrgansiaatioServiceCall");
        headers(
                from(SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT),
                headers()
                        .get()
                        .path(SINGLE_ORGANISAATIO_PATH)
                        .casAuthenticationByAuthenticatedUser(organisaatioServiceCasServiceUrl)
      )
        .process(organisaatioCallInOutDebug)
        .to(trim(organisaatioServiceRestUrl))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<OrganisaatioYksityiskohtaisetTiedotDto>() { }))
        .onException(HttpOperationFailedException.class)
                .throwException(new NotFoundException("Organisaatio not found by OID."));
    }

    @Override
    public List<String> findAllOrganisaatioOids(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ORGANSIAATIO_OID_LIST_ENDPOINT,
                "", new HashMap<String, Object>(), requestContext, List.class);
    }

    @Override
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria,
            CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), ORGANSIAATIOHAKU_ENDPOINT,
                criteria, new HashMap<String, Object>(), requestContext, List.class);
    }

    @Override
    public OrganisaatioYksityiskohtaisetTiedotDto getdOrganisaatioByOid(String oid,
                                                                        CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(),
                SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT, "",
                headerValues()
                    .add("oid", oid)
                .map(), requestContext, OrganisaatioYksityiskohtaisetTiedotDto.class);

    }
}
