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

import com.fasterxml.jackson.core.type.TypeReference;
import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.config.UrlConfiguration;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DefaultOrganisaatioServiceRoute extends AbstractJsonToDtoRouteBuilder
            implements  OrganisaatioServiceRoute {
    private static final long serialVersionUID = -7145790430698404115L;

    private static final String SERVICE_CALL_ORGANSIAATIO_POSTFIX = ".OrgansiaatioServiceCall";

    private static final String ORGANSIAATIO_OID_LIST_ENDPOINT  =  "direct:organisaatioOidList";
    private static final String ORGANSIAATIOHAKU_ENDPOINT  =  "direct:organisaatioYhteystietohakuV2";
    private static final String SINGLE_ORGANSIAATIO_BY_OID_ENDPOINT  =  "direct:singleOrganisaatioByOid";
    private static final String ORGANISAATIO_HIERARCHY_BY_TYYPPI_ENDPOINT = "direct:organisaatioHierarchyByTyyppiHaku";

    private static final String ORGANISAATIO_HIERACHY_TYYPPI_PARAM = "organisaatiotyyppi";
    private static final String ORGANISAATIO_HIERACHY_VAIN_AKTIIVISET_PARAM = "vainAktiiviset";

    private static final long HAKU_TIMEOUT_MINUTES = 10L;

    @Autowired
    private UrlConfiguration urlConfiguration;

    @Override
    public void configure() {
        buildOrganisaatioOidList();
        buildOrganisaatioHaku();
        buildSingleOrganisaatioTiedot();
        buildOrganisaatioHierarchyHaku();
    }

    protected void buildOrganisaatioOidList() {
        Debugger organisaatioCallInOutDebug  =  debug(ORGANSIAATIO_OID_LIST_ENDPOINT +
                SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(ORGANSIAATIO_OID_LIST_ENDPOINT),
                headers()
                        .get()
                .retry(3)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(urlConfiguration.getProperty("organisaatio-service.organisaatio")))
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
                .retry(3)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(urlConfiguration.getProperty("organisaatio-service.organisaatio.yhteystiedot.search"),
                // wait for 10 minutes maximum:
                HAKU_TIMEOUT_MINUTES * SECONDS_IN_MINUTE * MILLIS_IN_SECOND))
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
                .retry(3)
        )
        .process(organisaatioCallInOutDebug)
        .recipientList(simple(uri(urlConfiguration.getProperty("organisaatio-service.organisaatio.byOid",
                "$simple{in.headers.oid}"))))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<OrganisaatioDetailsDto>() {}));
    }

    protected void buildOrganisaatioHierarchyHaku() {
        Debugger authenticationCallInOutDebug  =  debug(ORGANISAATIO_HIERARCHY_BY_TYYPPI_ENDPOINT
                +SERVICE_CALL_ORGANSIAATIO_POSTFIX);
        headers(
                from(ORGANISAATIO_HIERARCHY_BY_TYYPPI_ENDPOINT),
                headers()
                        .get()
                        .param(ORGANISAATIO_HIERACHY_TYYPPI_PARAM).optional().valueFromBody().toQuery()
                        .param(ORGANISAATIO_HIERACHY_VAIN_AKTIIVISET_PARAM).value(true).toQuery()
                .retry(3)
        )
        .process(authenticationCallInOutDebug)
        .to(uri(urlConfiguration.getProperty("organisaatio-service.organisaatio.search")))
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
                ORGANISAATIO_HIERARCHY_BY_TYYPPI_ENDPOINT, tyyppi,
                headerValues().map(), requestContext, OrganisaatioHierarchyResultsDto.class);
    }
}
