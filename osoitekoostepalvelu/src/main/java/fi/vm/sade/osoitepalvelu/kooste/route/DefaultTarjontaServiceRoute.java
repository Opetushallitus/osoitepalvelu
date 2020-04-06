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

import fi.vm.sade.properties.OphProperties;
import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoulutusCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakuResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import fi.vm.sade.properties.OphProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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

    private static final long TIMEOUT_MINUTES = 30L;

    @Autowired
    private OphProperties properties;

    @Override
    public void configure() {
        buildTarjontaKoulutusRoute();
    }

    protected void buildTarjontaKoulutusRoute() {
        AbstractJsonToDtoRouteBuilder.Debugger tarjontaCallInOutDebug = debug(TARJONTA_KOULUTUS_SEARCH_ENDPOINT +
                SERVICE_CALL_TARJONTA_POSTFIX);

        headers(from(TARJONTA_KOULUTUS_SEARCH_ENDPOINT),
            headers()
                .get()
                .param(TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM).optional().valueFromHeader().toQuery()
                .param(TARJONTA_KOULUTUS_OPETUSKIELET_PARAM).optional().listFromHeader().toQuery()
                .param(TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM).optional().listFromHeader().toQuery()
                .param(TARJONTA_KOULUTUS_OPINTOALAS_PARAM).optional().listFromHeader().toQuery()
                .param(TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM).optional().listFromHeader().toQuery()
                .param(TARJONTA_KOULUTUS_KOULUTUS_PARAM).optional().listFromHeader().toQuery()
                .retry(2)
        )
        .process(tarjontaCallInOutDebug)
        .to(uri(properties.getProperty("tarjonta-service.koulutus.search"),
                TIMEOUT_MINUTES*SECONDS_IN_MINUTE*MILLIS_IN_SECOND))
        .process(tarjontaCallInOutDebug)
        .process(jsonToDto(new TypeReference<TarjontaKoulutusHakuResultDto>() {}));
    }

    @Override
    @SuppressWarnings("unchecked")
    public TarjontaKoulutusHakuResultDto findKoulutukset(KoulutusCriteriaDto criteria,
            CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), TARJONTA_KOULUTUS_SEARCH_ENDPOINT, "",
                headerValues()
                    .add(TARJONTA_KOULUTUS_KOULUTUSLAJI_PARAM,   criteria.getKoulutuslaji())
                    .add(TARJONTA_KOULUTUS_OPETUSKIELET_PARAM,   criteria.getOpetuskielet())
                    .add(TARJONTA_KOULUTUS_KOULUTUSALAS_PARAM,   criteria.getKoulutusalakoodis())
                    .add(TARJONTA_KOULUTUS_OPINTOALAS_PARAM,     criteria.getOpintoalakoodis())
                    .add(TARJONTA_KOULUTUS_KOULUTUSTYYPPI_PARAM, criteria.getKoulutustyyppis())
                    .add(TARJONTA_KOULUTUS_KOULUTUS_PARAM,       criteria.getKoulutuskoodis())
                .map(),
                requestContext, TarjontaKoulutusHakuResultDto.class);
    }

}
