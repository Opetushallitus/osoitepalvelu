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

import fi.vm.sade.osoitepalvelu.kooste.dao.koodistoCache.KoodistoCacheRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodiItem;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodistoCache;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.converter.KoodistoDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route.KoodistoRoute;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service, jonka kautta voidaan hakea koodiston eri arvojoukkoja.
 */
@Service
public class DefaultKoodistoService implements KoodistoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KoodistoRoute koodistoRoute;

    @Autowired
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Autowired
    private KoodistoDtoConverter dtoConverter;

    @Autowired(required = false)
    private KoodistoCacheRepository koodistoCacheRepository;

    @Value("#{config.cacheTimeoutMillis}")
    private long cacheTimeoutMillis;

    @Override
    public List<UiKoodiItemDto> findOppilaitosTyyppiOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.OPPILAITOSTYYPPI);
    }

    @Override
    public List<UiKoodiItemDto> findOmistajaTyyppiOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.OMISTAJATYYPPI);
    }

    @Override
    public List<UiKoodiItemDto> findVuosiluokkaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.VUOSILUOKAT);
    }

    @Override
    public List<UiKoodiItemDto> findMaakuntaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.MAAKUNTA);
    }

    @Override
    public List<UiKoodiItemDto> findKuntaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KUNTA);
    }

    @Override
    public List<UiKoodiItemDto> findTutkintoTyyppiOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.TUTKINTOTYYPPI);
    }

    @Override
    public List<UiKoodiItemDto> findTutkintoOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.TUTKINTO);
    }

    @Override
    public List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutuksenKieliOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusAsteOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSASTEKELA);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSTOIMIJA);
    }

    @Override
    public List<UiKoodiItemDto> findOpintoAlaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.OPINTOALAOPH2002);
    }

    @Override
    public List<UiKoodiItemDto> findAlueHallintoVirastoOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
    }

    @Override
    public List<UiKoodiItemDto> findKayttooikeusryhmas(final Locale locale) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                List<KayttooikesuryhmaDto> kayttoikeusryhmas = authenticationServiceRoute.findKayttooikeusryhmas();
                return dtoConverter.convert( kayttoikeusryhmas, new ArrayList<UiKoodiItemDto>(), UiKoodiItemDto.class,
                        locale );
            }
        }, KoodistoTyyppi.KAYTTOOIKEUSRYHMA, locale);
    }

    protected interface Cacheable<T> {
        T get();
    }

    protected List<UiKoodiItemDto> cached(Cacheable<List<UiKoodiItemDto>> provider, KoodistoTyyppi tyyppi, Locale locale) {
        KoodistoCache.KoodistoTyyppi cacheType = KoodistoCache.KoodistoTyyppi.valueOf(tyyppi.name());
        long cacheLiveTime = cacheTimeoutMillis;
        boolean cacheUsed = koodistoCacheRepository != null && cacheLiveTime >= 0;
        if (!cacheUsed) {
            logger.info("CACHE DISABLED.");
            return provider.get();
        }
        KoodistoCache cache = koodistoCacheRepository.findCacheByTypeAndLocale(cacheType, locale);
        boolean refresh = cache == null || cache.getUpdatedAt().plus(cacheLiveTime).compareTo(new DateTime()) < 0;
        if (cache == null) {
            cache = new KoodistoCache();
            cache.setKey(new KoodistoCache.CacheKey(cacheType, locale));
        }
        List<UiKoodiItemDto> items;
        if (refresh) {
            items = provider.get();
            cache.setItems(dtoConverter.convert(items, new ArrayList<KoodiItem>(), KoodiItem.class));
            cache.setUpdatedAt(new DateTime());
            koodistoCacheRepository.save(cache);
            logger.info("SAVED CACHED ITEMS FOR KoodistoTyyppi: " + tyyppi);
        } else {
            items = dtoConverter.convert(cache.getItems(), new ArrayList<UiKoodiItemDto>(), UiKoodiItemDto.class);
            logger.info("GOT CACHED RESULT FOR KoodistoTyyppi: " + tyyppi + " updated at " + cache.getUpdatedAt());
        }
        return items;
    }

    protected List<UiKoodiItemDto> findKoodistoByTyyppi(final Locale locale, final KoodistoTyyppi tyyppi) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                KoodistoVersioDto koodistoVersio = haeViimeisinVoimassaOlevaKoodistonVersio(tyyppi);
                if (koodistoVersio == null) {
                    return new ArrayList<UiKoodiItemDto>(); // Palautetaan tässä
                                                            // tyhjä lista
                }
                List<KoodiDto> arvot = koodistoRoute.haeKooditKoodistonVersiolleTyyppilla(tyyppi,
                        koodistoVersio.getVersio());
                arvot = filteroiAktiivisetKoodit(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return jarjestaNimetNousevasti(optiot);
            }
        }, tyyppi, locale);
    }

    /**
     * Filtteröi (eli poimii) kaikista koodiston arvoista uusimmat ja aktiiviset
     * versiot.
     * 
     * @param koodit
     *            Koodilista, joka filtteröi
     * @param voimassaPvm
     *            Päivämäärä, jonka avulla päätellään, onko koodin arvo
     *            voimassa.
     * @return Suodatettu lista, jossa mukana vain voimassa olevat arvot.
     */
    private List<KoodiDto> filteroiAktiivisetKoodit(List<KoodiDto> koodit, LocalDate voimassaPvm) {
        HashMap<String, KoodiDto> aktiivisetMap = new HashMap<String, KoodiDto>();
        for (KoodiDto koodi : koodit) {
            if (koodi.isVoimassaPvm(voimassaPvm) && KoodistoTila.isAktiivinenTila(koodi.getTila())) {
                // Päivitetään tässä versiotieto
                String mapKoodiId = koodi.getKoodiArvo();
                KoodiDto aikaisempiVersio = aktiivisetMap.get(mapKoodiId);
                // Onko eka versio tai uudempi versionumero kyseessä?
                if (aikaisempiVersio == null || koodi.getVersio() > aikaisempiVersio.getVersio()) {
                    // Tuoreempi versio -> Käytetään sitten tätä uusinta arvoa
                    // koodista
                    aktiivisetMap.put(mapKoodiId, koodi);
                }
            }
        }
        return new ArrayList<KoodiDto>(aktiivisetMap.values());
    }

    /**
     * Järjestää koodiston arvot nimen perusteella nousevasti.
     * 
     * @param values
     *            Koodiston arvot, jotka tulisi järjestää.
     * @return
     */
    private List<UiKoodiItemDto> jarjestaNimetNousevasti(List<UiKoodiItemDto> values) {
        Collections.sort(values, new Comparator<UiKoodiItemDto>() {
            @Override
            public int compare(UiKoodiItemDto koodiA, UiKoodiItemDto koodiB) {
                return koodiA.getNimi().compareTo(koodiB.getNimi());
            }
        });
        return values;
    }

    @Override
    public Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllKoodistos(Locale locale) {
        Map<KoodistoTyyppi, List<UiKoodiItemDto>> koodistotMap = new HashMap<KoodistoTyyppi, List<UiKoodiItemDto>>();
        KoodistoTyyppi[] tuetutKoodistot = KoodistoTyyppi.values();
        for (KoodistoTyyppi tyyppi : tuetutKoodistot) {
            List<UiKoodiItemDto> koodistonArvojoukko = findKoodistoByTyyppi(locale, tyyppi);
            koodistotMap.put(tyyppi, koodistonArvojoukko);
        }
        return koodistotMap;
    }

    /**
     * Hakee viimeisimmän tänään voimassa olevan koodiston versiotiedot.
     * 
     * @param tyyppi
     *            Koodiston tyyppi, eli koodiston tunniste.
     * @return Voimassa oleva koodiston versio tai null, jos tällaista versiota
     *         ei löydy.
     */
    private KoodistoVersioDto haeViimeisinVoimassaOlevaKoodistonVersio(KoodistoTyyppi tyyppi) {
        List<KoodistoVersioDto> versiot = koodistoRoute.haeKoodistonVersiot(tyyppi);
        long maxVersionNumber = -1L;
        KoodistoVersioDto versioVoimassa = null;
        if (versiot != null && versiot.size() > 0) {
            LocalDate tanaan = new LocalDate();
            for (KoodistoVersioDto versio : versiot) {
                // Onko koodi aktiivinen ja tänään voimassa?
                if (versio.isVoimassaPvm(tanaan) && KoodistoTila.isAktiivinenTila(versio.getTila())) {
                    // Etsitään maksimia versionumerosta
                    if (versio.getVersio() > maxVersionNumber) {
                        maxVersionNumber = versio.getVersio();
                        versioVoimassa = versio;
                    }
                }
            }
        }
        return versioVoimassa;
    }

    public long getCacheTimeoutMillis() {
        return cacheTimeoutMillis;
    }

    public void setCacheTimeoutMillis(long cacheTimeoutMillis) {
        this.cacheTimeoutMillis = cacheTimeoutMillis;
    }
}
