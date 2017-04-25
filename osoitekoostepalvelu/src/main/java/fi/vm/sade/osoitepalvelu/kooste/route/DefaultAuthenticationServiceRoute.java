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
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static final String OPPIJANUMEROREKISTERI_SERVICE_CALL_POSTFIX = ".OppijanumerorekisteriServiceCall";
    private static final String KAYTTOOIKEUS_SERVICE_CALL_POSTFIX = ".KayttooikeusServiceCall";

    private static final String ROUTE_KAYTTOOIKESURYHMAS  =  "direct:findKayttoikeusryhmas";

    private static final String ROUTE_HENKILOS  =  "direct:henkiloList";

    private static final String HENKILOS_PASSIVOITU_PARAM_NAME = "passivoitu";
    private static final String HENKILOS_DUPLIKAATTI_PARAM_NAME = "duplikaatti";
    private static final String HENKILOS_ORGANISAATIOOIDS_PARAM_NAME = "organisaatioOids";
    private static final String HENKILOS_KAYTTOOIKEUSRYHMAS_PARAM_NAME = "kayttoOikeusRyhmaNimet";
    private static final String HENKILOS_HENKILOTYYPPI_PARAM = "tyyppi";
    private static final String HENKILOS_HENKILOTYYPPI_VIRKAILIJA = "VIRKAILIJA";

    private static final String ROUTE_HENKILO = "direct:henkilo";

    @Autowired
    private UrlConfiguration urlConfiguration;

    @Override
    public void configure() {
        buildKayttoOikeusryhmas();
        buildHenkilo();
        buildHenkiloList();
    }

    private String camelUrl(String key, String... params) {
        String url = urlConfiguration.url(key);
        for (int i = 0; i < params.length; ++i) {
            url = url.replace("$" + (i + 1), params[i]);
        }
        return url;
    }

    protected void buildHenkilo() {
        Debugger oppijanumerorekisteriCallInOutDebug  =  debug(ROUTE_HENKILO + OPPIJANUMEROREKISTERI_SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILO),
            headers()
                .get()
                .casAuthenticationByAuthenticatedUser(
                        urlConfiguration.url("oppijanumerorekisteri-service.security-check")
                )
                .retry(3)
        )
        .process(oppijanumerorekisteriCallInOutDebug)
        .recipientList(simple(uri(camelUrl("oppijanumerorekisteri-service.henkilo.byOid",
            "$simple{in.body}"),
                HENKILO_TIMEOUT_MILLIS)))
        .process(oppijanumerorekisteriCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<HenkiloDetailsDto>() {}));
    }

    protected void buildHenkiloList() {
        Debugger kayttooikeusCallInOutDebug  =  debug(ROUTE_HENKILOS + OPPIJANUMEROREKISTERI_SERVICE_CALL_POSTFIX);
        headers(
            from(ROUTE_HENKILOS),
            headers()
                .post()
                    .jsonRequstBody()
                .casAuthenticationByAuthenticatedUser(
                        urlConfiguration.url("oppijanumerorekisteri-service.security-check")
                )
                .retry(3)
        )
        .process(kayttooikeusCallInOutDebug)
        .to(uri(urlConfiguration.url("oppijanumerorekisteri-service.henkilo.haku"),
                HENKILOLIST_TIMEOUT_MILLIS)) // wait for 10 minutes maximum
        .process(kayttooikeusCallInOutDebug)
        .process(saveSession())
        .process(jsonToDto(new TypeReference<List<HenkiloListResultDto>>() {}));
    }

    protected void buildKayttoOikeusryhmas() {
        Debugger kayttooikeusCallInOutDebug  =  debug(ROUTE_KAYTTOOIKESURYHMAS + KAYTTOOIKEUS_SERVICE_CALL_POSTFIX);
        headers(
                from(ROUTE_KAYTTOOIKESURYHMAS),
                headers()
                        .get()
                        .casAuthenticationByAuthenticatedUser(
                                urlConfiguration.url("kayttooikeus-service.security-check")
                        )
                .retry(3)
        )
        .process(kayttooikeusCallInOutDebug)
        .to(uri(urlConfiguration.url("kayttooikeus-service.kayttoikeusryhma")))
        .process(kayttooikeusCallInOutDebug)
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
        return details;
    }

    @Override
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(HENKILOS_PASSIVOITU_PARAM_NAME, false);
        body.put(HENKILOS_DUPLIKAATTI_PARAM_NAME, false);
        body.put(HENKILOS_HENKILOTYYPPI_PARAM, HENKILOS_HENKILOTYYPPI_VIRKAILIJA);
        body.put(HENKILOS_ORGANISAATIOOIDS_PARAM_NAME, emptyToNull(criteria.getOrganisaatioOids()));
        body.put(HENKILOS_KAYTTOOIKEUSRYHMAS_PARAM_NAME, emptyToNull(criteria.getKayttoOikeusRayhmas()));

        @SuppressWarnings("unchecked")
        List<HenkiloListResultDto> searchResults = sendBodyHeadersAndProperties(getCamelTemplate(),
                ROUTE_HENKILOS, body, headerValues().map(), requestContext, List.class);
        return searchResults;
    }

    private <T> Collection<T> emptyToNull(Collection<T> collection) {
        return collection == null || collection.isEmpty() ? null : collection;
    }
}