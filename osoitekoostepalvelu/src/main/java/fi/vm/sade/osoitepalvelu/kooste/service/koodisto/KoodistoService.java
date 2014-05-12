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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service -rajapinta, jonka kautta pystyy hakemaan koodiston arvoja koodiston
 * tyypin perusteella. Oletusarvoisesti tämä rajapinta hakee kaikista koodeista
 * viimeisimmän voimassa olevan version.
 */
public interface KoodistoService {

    List<UiKoodiItemDto> findOppilaitosTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findOmistajaTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findVuosiluokkaOptions(Locale locale);

    List<UiKoodiItemDto> findMaakuntaOptions(Locale locale);

    List<UiKoodiItemDto> findKuntaOptions(Locale locale);

    List<UiKoodiItemDto> findKuntasByMaakuntaUri(Locale locale, String maakuntaUri);

    UiKoodiItemDto findKuntaByKoodiUri(Locale locale, String koodiUri);

    List<UiKoodiItemDto> findPostinumeroOptions(Locale locale);

    UiKoodiItemDto findPostinumeroByKoodiUri(Locale locale, String koodiUri);

    List<UiKoodiItemDto> findTutkintoTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findTutkintoOptions(Locale locale);

    List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutuksenKieliOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutusAsteOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(Locale locale);

    List<UiKoodiItemDto> findOpintoAlaOptions(Locale locale);

    List<UiKoodiItemDto> findAlueHallintoVirastoOptions(Locale locale);

    List<UiKoodiItemDto> findKayttooikeusryhmas(Locale locale);

    List<UiKoodiItemDto> findTutkintotoimikuntaRooliOptions(Locale locale);

    List<UiKoodiItemDto> findTutkintotoimikuntaOptions(Locale locale);

    /**
     * Hakee kaikkien koodistojen arvot yhdellä kertaa ja paulattaa näistä
     * mapin. Mapista halutun koodiston voi hakea KoodistoTyyppi enumin arvon
     * perustella. Huom: Metodi suorittaa useita kutsuja ulkoiseen
     * koodistopalveluun.
     * 
     * @param locale
     *            Koodistojen arvojen nimien ja kuvauksen lokalisoinnin locale.
     * @return Mappi, joka sisältää kaikkien tuettujen koodistojen arvot.
     */
    Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllKoodistos(Locale locale);

    void purgeTutkintotoimikuntaCaches();

    /**
     * Purges all koodisto related MongoDB and memory caches.
     */
    void purgeCaches();
}
