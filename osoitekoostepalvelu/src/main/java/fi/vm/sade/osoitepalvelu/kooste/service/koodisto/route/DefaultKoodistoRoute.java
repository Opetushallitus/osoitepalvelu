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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.HeaderBuilder;
import org.apache.camel.Exchange;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoVersioDto;

/**
 * Koodisto-palvelun Camel-reittien toteutus.
 */
@Component
public class DefaultKoodistoRoute extends AbstractJsonToDtoRouteBuilder implements KoodistoRoute {

    private static final String REITTI_HAE_KOODISTON_KOODIT = "direct:haeKoodistonKoodit";
    private static final String REITTI_HAE_KOODISTO_VERSION_KOODIT = "direct:haeKoodistoVersionKoodit";
    private static final String REITTI_HAE_KOODISTON_VERSIOT = "direct:haeKoodistonVersiot";

    @Value("${valintalaskentakoostepalvelu.koodiService.rest.url}")
    private String koodistoUri;

    private boolean findCounterUsed = false;
    private AtomicLong findCounter = new AtomicLong(0L);

    @Override
    public void configure() throws Exception {
        koodistoUri = trim(koodistoUri);

        // Reitti, joka hakee tietyn koodiston koodit
        TypeReference<List<KoodiDto>> dtoType = new TypeReference<List<KoodiDto>>() {};
        fromHttpGetToDtos(REITTI_HAE_KOODISTON_KOODIT, koodistoUri, Exchange.HTTP_PATH,
                simple("${in.headers.koodistoTyyppi}/koodi"), dtoType);

        // Seuraava reitti hakee tietyn koodistoversion kaikki koodit
        HeaderBuilder headers = new HeaderBuilder();
        headers.add(Exchange.HTTP_PATH, simple("${in.headers.koodistoTyyppi}/koodi"));
        headers.add(Exchange.HTTP_QUERY, simple("koodistoVersio=${in.headers.koodistoVersio}"));
        fromHttpGetToDtos(REITTI_HAE_KOODISTO_VERSION_KOODIT, koodistoUri, headers, dtoType);

        // Reitti, joka hakee koodiston versiotiedot
        TypeReference<List<KoodistoVersioDto>> versioDtoType = new TypeReference<List<KoodistoVersioDto>>() {};
        fromHttpGetToDtos(REITTI_HAE_KOODISTON_VERSIOT, koodistoUri, Exchange.HTTP_PATH,
                simple("${in.headers.koodistoTyyppi}"), versioDtoType);
    }

    @Override
    public List<KoodiDto> findKooditKoodistonTyyppilla(KoodistoTyyppi koodistoTyyppi) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodit = getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_KOODIT, "",
                "koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
        return koodit;
    }

    @Override
    public List<KoodiDto> findKooditKoodistonVersiolleTyyppilla(KoodistoTyyppi koodistoTyyppi, long versio) {
        // Asetataan tarvittavat parametrit
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("koodistoTyyppi", koodistoTyyppi.getUri());
        parameters.put("koodistoVersio", "" + versio);
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodit = getCamelTemplate().requestBodyAndHeaders(REITTI_HAE_KOODISTO_VERSION_KOODIT, "",
                parameters, List.class);
        if (isFindCounterUsed()) {
            findCounter.incrementAndGet();
        }
        return koodit;
    }

    @Override
    public List<KoodistoVersioDto> findKoodistonVersiot(KoodistoTyyppi koodistoTyyppi) {
        @SuppressWarnings("unchecked")
        List<KoodistoVersioDto> versiot = getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_VERSIOT, "",
                "koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
        return versiot;
    }

    @Override
    public boolean isFindCounterUsed() {
        return findCounterUsed;
    }

    @Override
    public void setFindCounterUsed(boolean findCounterUsed) {
        this.findCounterUsed = findCounterUsed;
    }

    @Override
    public long getFindCounterValue() {
        return findCounter.longValue();
    }
}
