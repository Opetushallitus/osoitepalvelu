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
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOsoitepalveluResultsDto;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * User: ratamaa
 * Date: 4/9/14
 * Time: 5:09 PM
 */
@Component
public class DefaultAituRoute extends AbstractJsonToDtoRouteBuilder
        implements AituRoute {
    private static final long serialVersionUID = 832138099426375308L;
    private static final String SERVICE_CALL_AITU_POSTFIX = ".AituServiceCall";
    private static final String AITU_OSOITEPALVELU_ENDPOINT = "direct:aituRoute";
    private static final String AITU_OSOITEPALVELU_PATH = "/osoitepalvelu";

    private static final String CAS_TICKET_QUERY_PARAM = "ticket";
    private static final long TIMEOUT_MINUTES = 30L;

    @Value("${aitu.rest.uri}")
    private String aituRestUrl;

    @Value("${cas.service.aitu-service}")
    private String aituServiceCasServiceUrl;

    @Override
    public void configure() {
        buildAituRoute();
    }

    protected void buildAituRoute() {
        Debugger organisaatioCallInOutDebug  =  debug(AITU_OSOITEPALVELU_ENDPOINT +SERVICE_CALL_AITU_POSTFIX);
        headers(
            from(AITU_OSOITEPALVELU_ENDPOINT),
            headers()
                    .get().path(AITU_OSOITEPALVELU_PATH)
                    .param(CAS_TICKET_QUERY_PARAM).optional().value(header(CasTicketProvider.CAS_HEADER)).toQuery()
                    .casAuthenticationByAuthenticatedUser(aituServiceCasServiceUrl)
                    .retry(2)
        )
        .process(organisaatioCallInOutDebug)
        .to(uri(aituRestUrl, TIMEOUT_MINUTES*SECONDS_IN_MINUTE*MILLIS_IN_SECOND))
        .process(organisaatioCallInOutDebug)
        .process(jsonToDto(new TypeReference<AituOsoitepalveluResultsDto>() {}));
    }

    @Override
    public AituOsoitepalveluResultsDto findAituResults(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), AITU_OSOITEPALVELU_ENDPOINT,
                "", new HashMap<String, Object>(), requestContext, AituOsoitepalveluResultsDto.class);
    }
}
