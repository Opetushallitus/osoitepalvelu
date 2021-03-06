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
import fi.vm.sade.osoitepalvelu.kooste.config.UrlConfiguration;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoVersioDto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Koodisto-palvelun Camel-reittien toteutus.
 */
@Component
public class DefaultKoodistoRoute extends AbstractJsonToDtoRouteBuilder implements KoodistoRoute {

    private static final long serialVersionUID = -7002852859580790344L;
    private static final String REITTI_HAE_KOODISTON_KOODIT  =  "direct:haeKoodistonKoodit";
    private static final String REITTI_HAE_KOODISTO_VERSION_KOODIT  =  "direct:haeKoodistoVersionKoodit";
    private static final String REITTI_HAE_KOODISTON_VERSIOT  =  "direct:haeKoodistonVersiot";
    private static final String REITTI_SIALTYY_YLAKOODIS  =  "direct:sisaltyYlakoodis";
    private static final String REITTI_SIALTYY_ALAKOODIS  =  "direct:sisaltyAlakoodis";

    private static final long KOODISTO_TIMEOUT = 5L * SECONDS_IN_MINUTE * MILLIS_IN_SECOND;

    @Autowired
    private UrlConfiguration urlConfiguration;

    // For cache testing:
    private boolean findCounterUsed  =  false;
    private AtomicLong findCounter  =  new AtomicLong(0L);

    @Override
    public void configure() {
        buildKoodistonKoodit();
        buildKoodiversioKoodis();
        buildKaikkiVersiotiedot();
        buildSisaltyyYlakoodis();
        buildSisaltyyAlakoodis();
    }

    protected void buildKaikkiVersiotiedot() {
        // Reitti, joka hakee koodiston versiotiedot
        fromHttpGetToDtosWithRecipientList(
                REITTI_HAE_KOODISTON_VERSIOT,
                uri(urlConfiguration.getProperty("koodisto-service.byUri",
                        "$simple{in.headers.koodistoTyyppi}"), KOODISTO_TIMEOUT),
                headers().retry(3),
                new TypeReference<List<KoodistoVersioDto>>() { }
        );
    }

    protected void buildKoodiversioKoodis() {
        // Seuraava reitti hakee tietyn koodistoversion kaikki koodit
        fromHttpGetToDtosWithRecipientList(
                REITTI_HAE_KOODISTO_VERSION_KOODIT,
                uri(urlConfiguration.getProperty("koodisto-service.byUri.koodi",
                        "$simple{in.headers.koodistoTyyppi}"), KOODISTO_TIMEOUT),
                headers()
                        .query("koodistoVersio = ${in.headers.koodistoVersio}")
                        .retry(3),
                new TypeReference<List<KoodiDto>>() { }
        );
    }

    protected void buildKoodistonKoodit() {
        // Reitti, joka hakee tietyn koodiston koodit
        fromHttpGetToDtosWithRecipientList(
                REITTI_HAE_KOODISTON_KOODIT,
                uri(urlConfiguration.getProperty("koodisto-service.byUri.koodi",
                        "$simple{in.headers.koodistoTyyppi}"), KOODISTO_TIMEOUT),
                headers().retry(3),
                new TypeReference<List<KoodiDto>>() { }
        );
    }

    protected void buildSisaltyyYlakoodis() {
        fromHttpGetToDtosWithRecipientList(REITTI_SIALTYY_YLAKOODIS,
                uri(urlConfiguration.getProperty("koodisto-service.relaatio.ylakoodi.byUri",
                        "$simple{in.headers.koodiUri}"), KOODISTO_TIMEOUT),
                headers().retry(3),
                new TypeReference<List<KoodiDto>>() { });
    }

    protected void buildSisaltyyAlakoodis() {
        fromHttpGetToDtosWithRecipientList(REITTI_SIALTYY_ALAKOODIS,
                uri(urlConfiguration.getProperty("koodisto-service.relaatio.alakoodi.byUri",
                        "$simple{in.headers.koodiUri}"), KOODISTO_TIMEOUT),
                headers().retry(3),
                new TypeReference<List<KoodiDto>>() { });
    }

    @Override
    public List<KoodiDto> findKooditKoodistonTyyppilla(KoodistoTyyppi koodistoTyyppi) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodit  =  getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_KOODIT, "",
                "koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
        return koodit;
    }

    @Override
    public List<KoodiDto> findKooditKoodistonVersiolleTyyppilla(KoodistoTyyppi koodistoTyyppi, long versio) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodit  =  getCamelTemplate().requestBodyAndHeaders(REITTI_HAE_KOODISTO_VERSION_KOODIT, "",
                headerValues()
                        .add("koodistoTyyppi", koodistoTyyppi.getUri())
                        .add("koodistoVersio", ""  +  versio)
                .map(), List.class);
        if (isFindCounterUsed()) {
            findCounter.incrementAndGet();
        }
        return koodit;
    }

    @Override
    public List<KoodistoVersioDto> findKoodistonVersiot(KoodistoTyyppi koodistoTyyppi) {
        @SuppressWarnings("unchecked")
        List<KoodistoVersioDto> versiot  =  getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_VERSIOT, "",
                "koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
        return versiot;
    }

    @Override
    public List<KoodiDto> findKoodisByParent(String koodiUri) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodis  =  getCamelTemplate().requestBodyAndHeader(REITTI_SIALTYY_YLAKOODIS, "",
                "koodiUri", koodiUri, List.class);
        return koodis;
    }

    @Override
    public List<KoodiDto> findKoodisByChild(String koodiUri) {
        @SuppressWarnings("unchecked")
        List<KoodiDto> koodis  =  getCamelTemplate().requestBodyAndHeader(REITTI_SIALTYY_ALAKOODIS, "",
                "koodiUri", koodiUri, List.class);
        return koodis;
    }

    @Override
    public boolean isFindCounterUsed() {
        return findCounterUsed;
    }

    @Override
    public void setFindCounterUsed(boolean findCounterUsed) {
        this.findCounterUsed  =  findCounterUsed;
    }

    @Override
    public long getFindCounterValue() {
        return findCounter.longValue();
    }
}
