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
import fi.vm.sade.osoitepalvelu.kooste.config.UrlConfiguration;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoulutusCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakuResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import fi.vm.sade.properties.OphProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
@Component
public class DefaultTarjontaServiceRoute extends AbstractJsonToDtoRouteBuilder
        implements TarjontaServiceRoute {
    private static final long serialVersionUID = 1L;

    private static final String SERVICE_CALL_TARJONTA_POSTFIX = ".TarjontaServiceCall";
    private static final String TARJONTA_KOULUTUS_SEARCH_ENDPOINT = "direct:tarjontaKoulutusRoute";

    private static final String TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM   = "koulutuslaji";
    private static final String TARJONTA_KOULUTUS_OPETUSKIELET_PARAM   = "opetuskielet";
    private static final String TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM   = "koulutusalakoodi";
    private static final String TARJONTA_KOULUTUS_OPINTOALAS_PARAM     = "opintoalakoodi";
    private static final String TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM = "koulutustyyppi";
    private static final String TARJONTA_KOULUTUS_KOULUTUS_PARAM       = "koulutuskoodi";

    @Autowired
    private UrlConfiguration urlConfiguration;

    @Override
    public void configure() {
        buildTarjontaKoulutusRoute();
    }

    protected void buildTarjontaKoulutusRoute() {
        AbstractJsonToDtoRouteBuilder.Debugger tarjontaCallInOutDebug = debug(TARJONTA_KOULUTUS_SEARCH_ENDPOINT +
                SERVICE_CALL_TARJONTA_POSTFIX);

        headers(from(TARJONTA_KOULUTUS_SEARCH_ENDPOINT),
            headers().get().retry(2)
        )
        .process(tarjontaCallInOutDebug)
        .recipientList(simple(urlConfiguration.getProperty("tarjonta-service.koulutus.search",
                "$simple{in.headers.searchparams}")))
        .process(tarjontaCallInOutDebug)
        .process(jsonToDto(new TypeReference<TarjontaKoulutusHakuResultDto>() {}));
    }

    @Override
    @SuppressWarnings("unchecked")
    public TarjontaKoulutusHakuResultDto findKoulutukset(KoulutusCriteriaDto criteria,
            CamelRequestContext requestContext) {

        List<String> headers = Arrays.asList(
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM, criteria)),
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_OPETUSKIELET_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_OPETUSKIELET_PARAM, criteria)),
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM, criteria)),
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_OPINTOALAS_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_OPINTOALAS_PARAM, criteria)),
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM, criteria)),
                createHttpGetQueryListParamsforHeader(TARJONTA_KOULUTUS_KOULUTUS_PARAM,
                        getHeaderValuesFromCriteria(TARJONTA_KOULUTUS_KOULUTUS_PARAM, criteria))
        );

        return sendBodyHeadersAndProperties(getCamelTemplate(), TARJONTA_KOULUTUS_SEARCH_ENDPOINT, "",
                headerValues()
                        .add("searchparams", headers.stream()
                                .filter(value -> !value.isEmpty())
                                .collect(Collectors.joining("&")))
                .map(),
                requestContext, TarjontaKoulutusHakuResultDto.class);
    }

    private String createHttpGetQueryListParamsforHeader(String headerParamName, List<String> list) {
        if (list == null && list.size() < 1) return "";

        return list.stream()
                .map(value -> formatSingleQueryParam(headerParamName, value))
                .collect(Collectors.joining("&"));
    }

    private String formatSingleQueryParam(String name, String value) {
        return name + "=" + value;
    }

    private List<String> getHeaderValuesFromCriteria(String headerName, KoulutusCriteriaDto criteria) {
        switch (headerName) {
            case TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM:
                return new ArrayList<String>() {{ criteria.getKoulutuslaji(); }};
            case TARJONTA_KOULUTUS_OPETUSKIELET_PARAM:
                return criteria.getOpetuskielet();
            case TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM:
                return criteria.getKoulutusalakoodis();
            case TARJONTA_KOULUTUS_OPINTOALAS_PARAM:
                return criteria.getOpintoalakoodis();
            case TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM:
                return criteria.getKoulutustyyppis();
            case TARJONTA_KOULUTUS_KOULUTUS_PARAM:
                return criteria.getKoulutuskoodis();
            default:
                return new ArrayList<>();
        }
    }
}
