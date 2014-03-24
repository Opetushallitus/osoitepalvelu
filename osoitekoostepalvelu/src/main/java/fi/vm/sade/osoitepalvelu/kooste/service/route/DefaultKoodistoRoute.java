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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoVersioDto;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Koodisto-palvelun Camel-reittien toteutus.
 */
@Component
public class DefaultKoodistoRoute extends AbstractJsonToDtoRouteBuilder implements KoodistoRoute {
    private static final String REITTI_HAE_KOODISTON_KOODIT = "direct:haeKoodistonKoodit";
    private static final String REITTI_HAE_KOODISTO_VERSION_KOODIT = "direct:haeKoodistoVersionKoodit";
    private static final String REITTI_HAE_KOODISTON_VERSIOT = "direct:haeKoodistonVersiot";
    private static final String REITTI_SIALTYY_YLAKOODIS = "direct:sisaltyYlakoodis";

    @Value("${koodiService.rest.url}")
    private String koodistoUri;

    // For cache testing:
    private boolean findCounterUsed = false;
    private AtomicLong findCounter = new AtomicLong(0L);

    @Override
    public void configure() throws Exception {
        buildKoodistonKoodit();
        buildKoodiversioKoodis();
        buildKaikkiVersiotiedot();
        buildSisaltyyYlakoodis();
    }

    protected void buildKaikkiVersiotiedot() {
        // Reitti, joka hakee koodiston versiotiedot
        fromHttpGetToDtos(REITTI_HAE_KOODISTON_VERSIOT, trim(koodistoUri),
                headers().path("${in.headers.koodistoTyyppi}"),
                new TypeReference<List<KoodistoVersioDto>>() {} );
    }

    protected void buildKoodiversioKoodis() {
        // Seuraava reitti hakee tietyn koodistoversion kaikki koodit
        fromHttpGetToDtos(REITTI_HAE_KOODISTO_VERSION_KOODIT, trim(koodistoUri),
                headers()
                    .path("${in.headers.koodistoTyyppi}/koodi")
                    .query("koodistoVersio=${in.headers.koodistoVersio}"),
                new TypeReference<List<KoodiDto>>() {});
    }

    protected void buildKoodistonKoodit() {
        // Reitti, joka hakee tietyn koodiston koodit
        fromHttpGetToDtos(REITTI_HAE_KOODISTON_KOODIT, trim(koodistoUri),
                headers().path("${in.headers.koodistoTyyppi}/koodi"),
                new TypeReference<List<KoodiDto>>() {});
    }

    protected void buildSisaltyyYlakoodis() {
        fromHttpGetToDtos(REITTI_SIALTYY_YLAKOODIS, trim(koodistoUri),
                headers().path("relaatio/sisaltyy-ylakoodit/${in.headers.koodiUri}"),
                new TypeReference<List<KoodiDto>>() {});
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
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodit = getCamelTemplate().requestBodyAndHeaders(REITTI_HAE_KOODISTO_VERSION_KOODIT, "",
                headerValues()
                        .add("koodistoTyyppi", koodistoTyyppi.getUri())
                        .add("koodistoVersio", "" + versio)
                .map(), List.class);
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
    public List<KoodiDto> findKoodisWithParent(String koodiUri) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodis = getCamelTemplate().requestBodyAndHeader(REITTI_SIALTYY_YLAKOODIS, "",
                "koodiUri", koodiUri, List.class);
        return koodis;
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
