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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.cache.KoodistoCacheRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodiItem;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodistoCache;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodistoCache.CacheKey;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.converter.KoodistoDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.KoodistoRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KayttooikesuryhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoTila;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoVersioDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.UiKoodiItemByKoodiUriPredicate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.UiKoodiItemByKoodiUriVersionPredicate;

/**
 * Service, jonka kautta voidaan hakea koodiston eri arvojoukkoja.
 */
@Service
public class DefaultKoodistoService extends AbstractService implements KoodistoService {
    private static final long serialVersionUID = 2428213877195977773L;

    private static final String ORGANISAATIO_TYYPPI_KOULUTUSTOIMIJA = "Koulutustoimija";

    @Autowired(required = false)
    private KoodistoRoute koodistoRoute;

    @Autowired(required = false)
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Autowired(required = false)
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired
    private KoodistoDtoConverter dtoConverter;

    @Autowired(required  =  false)
    private KoodistoCacheRepository koodistoCacheRepository;

    @Value("${koodisto.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;

    private Map<KoodistoCache.CacheKey, MemoryCacheHolder> memoryCache
             =  new HashMap<KoodistoCache.CacheKey, MemoryCacheHolder>();

    @Override
    public void purgeCaches() {
        this.memoryCache = new HashMap<CacheKey, MemoryCacheHolder>();
        if (this.koodistoCacheRepository != null) {
            this.koodistoCacheRepository.deleteAll();
        }
    }

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
    public List<UiKoodiItemDto> findKuntasByMaakuntaUri(final Locale locale, final String maakuntaUri) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                List<KoodiDto> arvot = koodistoRoute.findKoodisByParent(maakuntaUri);
                arvot = filterActiveKoodis(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return orderNimisAsc(optiot);
            }
        }, new KoodistoCache.CacheKey(maakuntaUri, locale));
    }

    @Override
    public UiKoodiItemDto findKuntaByKoodiUri(Locale locale, String koodiUri) {
        Iterator<UiKoodiItemDto> i  =  Collections2.filter(findKuntaOptions(locale),
                new UiKoodiItemByKoodiUriPredicate(koodiUri)).iterator();
        if (i.hasNext()) {
            return i.next();
        }
        return null;
    }

    @Override
    public List<UiKoodiItemDto> findPostinumeroOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.POSTINUMERO);
    }

    @Override
    public UiKoodiItemDto findPostinumeroByKoodiUri(Locale locale, String koodiUri) {
        if (!koodiUri.startsWith("posti_")) {
            koodiUri = "posti_"+koodiUri;
        }
        Iterator<UiKoodiItemDto> i = Collections2.filter(findPostinumeroOptions(locale),
                new UiKoodiItemByKoodiUriPredicate(koodiUri)).iterator();
        if (i.hasNext()) {
            UiKoodiItemDto item = i.next();
            if (item.getNimi() != null && !item.getNimi().contains("_missing!")) {
                return item;
            }
        }
        if (!EqualsHelper.areEquals(locale, DEFAULT_LOCALE)) {
            i = Collections2.filter(findPostinumeroOptions(DEFAULT_LOCALE),
                    new UiKoodiItemByKoodiUriPredicate(koodiUri)).iterator();
            if (i.hasNext()) {
                return i.next();
            }
        }
        return null;
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
    public UiKoodiItemDto findOppilaitoksenOpetuskieliByKoodiUri(Locale locale, String koodiUri) {
        Iterator<UiKoodiItemDto> i  =  Collections2.filter(findOppilaitoksenOpetuskieliOptions(locale),
                new UiKoodiItemByKoodiUriVersionPredicate(koodiUri)).iterator();
        if (i.hasNext()) {
            return i.next();
        }
        return null;
    }

    @Override
    public List<UiKoodiItemDto> findKieliOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KIELI);
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
    public List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(final Locale locale) {
        // return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSTOIMIJA); // out dated
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                OrganisaatioHierarchyResultsDto results = organisaatioServiceRoute
                        .findOrganisaatioHierachyByTyyppi(ORGANISAATIO_TYYPPI_KOULUTUSTOIMIJA,
                                new DefaultCamelRequestContext());
                return dtoConverter.convert(results.getOrganisaatiot(),
                        new ArrayList<UiKoodiItemDto>(), UiKoodiItemDto.class,
                        locale, KoodistoTyyppi.KOULUTUSTOIMIJA);
            }
        }, new KoodistoCache.CacheKey(KoodistoCache.KoodistoTyyppi.KOULUTUSTOIMIJA, locale));
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusAlaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSALAOPH2002);
    }

    @Override
    public List<UiKoodiItemDto> findOpintoAlaOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.OPINTOALAOPH2002);
    }

    @Override
    public List<UiKoodiItemDto> findOpintoalasByKoulutusalaAlaUri(final Locale locale, final String opintoalaUri) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                List<KoodiDto> arvot = koodistoRoute.findKoodisByChild(opintoalaUri);
                arvot = new ArrayList<KoodiDto>(Collections2.filter(arvot, new Predicate<KoodiDto>() {
                    public boolean apply(KoodiDto input) {
                        return input.getKoodisto().getTyyppi() == KoodistoTyyppi.OPINTOALAOPH2002;
                    }
                }));
                arvot = filterActiveKoodis(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return orderNimisAsc(optiot);
            }
        }, new KoodistoCache.CacheKey(opintoalaUri, locale));
    }

    @Override
    public List<UiKoodiItemDto> findOpintoAlasByKoulutusAlas(Locale locale, String[] koulutusalas) {
        if (koulutusalas == null) {
            return findOpintoAlaOptions(locale);
        }
        List<UiKoodiItemDto> koodiItems = new ArrayList<UiKoodiItemDto>();
        for (String koulutusala : koulutusalas) {
            koodiItems.addAll(findOpintoalasByKoulutusalaAlaUri(locale, koulutusala));
        }
        return orderNimisAsc(koodiItems);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUS);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusByOpintoalaUri(final Locale locale, final String opintoalaUri) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                List<KoodiDto> arvot = koodistoRoute.findKoodisByParent(opintoalaUri);
                arvot = new ArrayList<KoodiDto>(Collections2.filter(arvot, new Predicate<KoodiDto>() {
                    public boolean apply(KoodiDto input) {
                        return input.getKoodisto().getTyyppi() == KoodistoTyyppi.KOULUTUS;
                    }
                }));
                arvot = filterActiveKoodis(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return orderNimisAsc(optiot);
            }
        }, new KoodistoCache.CacheKey(opintoalaUri, locale));
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusTyyppiOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSTYYPPI);
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusByKoulutustyyppiUri(final Locale locale, final String koulutustyyppiUri) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                List<KoodiDto> arvot = koodistoRoute.findKoodisByParent(koulutustyyppiUri);
                arvot = new ArrayList<KoodiDto>(Collections2.filter(arvot, new Predicate<KoodiDto>() {
                    public boolean apply(KoodiDto input) {
                        return input.getKoodisto().getTyyppi() == KoodistoTyyppi.KOULUTUS;
                    }
                }));
                arvot = filterActiveKoodis(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return orderNimisAsc(optiot);
            }
        }, new KoodistoCache.CacheKey(koulutustyyppiUri, locale));
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusByOpintoalasOrTyyppis(Locale locale, String[] opintoalas, String[] tyyppis) {
        if (opintoalas == null && tyyppis == null) {
            return findKoulutusOptions(locale);
        }
        List<UiKoodiItemDto> itemsByOpintoala = new ArrayList<UiKoodiItemDto>();
        if (opintoalas != null) {
            for (String opintoala : opintoalas) {
                itemsByOpintoala.addAll(findKoulutusByOpintoalaUri(locale, opintoala));
            }
        }
        if (tyyppis != null) {
            List<UiKoodiItemDto> itemsByTyyppi = new ArrayList<UiKoodiItemDto>();
            for (String tyyppi : tyyppis) {
                itemsByTyyppi.addAll(findKoulutusByKoulutustyyppiUri(locale, tyyppi));
            }
            if (opintoalas != null) {
                return orderNimisAsc(CollectionHelper.mergeIntersected(itemsByOpintoala, itemsByTyyppi, UiKoodiItemDto.URI));
            }
            return itemsByTyyppi;
        }
        return itemsByOpintoala;
    }

    @Override
    public List<UiKoodiItemDto> findKoulutusLajiOptions(Locale locale) {
        return findKoodistoByTyyppi(locale, KoodistoTyyppi.KOULUTUSLAJI);
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
                List<KayttooikesuryhmaDto> kayttoikeusryhmas = authenticationServiceRoute
                        .findKayttooikeusryhmas(new DefaultCamelRequestContext());
                return dtoConverter.convert(kayttoikeusryhmas, new ArrayList<UiKoodiItemDto>(), UiKoodiItemDto.class,
                        locale);
            }
        }, new KoodistoCache.CacheKey(KoodistoCache.KoodistoTyyppi.valueOf(KoodistoTyyppi.KAYTTOOIKEUSRYHMA.name()),
                locale));
    }

    protected interface Cacheable<T> {
        T get();
    }

    protected static final class MemoryCacheHolder implements Serializable {
        private static final long serialVersionUID = -4046813860312365516L;

        private DateTime createdAt;
        private List<UiKoodiItemDto> items;

        public MemoryCacheHolder(DateTime createdAt, List<UiKoodiItemDto> items) {
            this.createdAt  =  createdAt;
            this.items  =  items;
        }

        public DateTime getCreatedAt() {
            return createdAt;
        }

        public List<UiKoodiItemDto> getItems() {
            return items;
        }
    }

    protected List<UiKoodiItemDto> cached(Cacheable<List<UiKoodiItemDto>> provider, KoodistoCache.CacheKey key) {
        boolean cacheUsed  =  isCacheUsed();
        if (!cacheUsed) {
            logger.debug("CACHE DISABLED.");
            return provider.get();
        }
        MemoryCacheHolder holder  =  memoryCache.get(key);
        if (holder != null && isCacheUsable(holder.getCreatedAt())) {
            // Hit memory cache:
            return holder.getItems();
        }
        KoodistoCache cache = koodistoCacheRepository.findOne(key);
        boolean refresh = cache == null || !isCacheUsable(cache.getUpdatedAt());
        if (cache == null) {
            cache = new KoodistoCache();
            cache.setKey(key);
        }
        List<UiKoodiItemDto> items;
        if (refresh) {
            items  =  provider.get();
            cache.setItems(dtoConverter.convert(items, new ArrayList<KoodiItem>(), KoodiItem.class));
            cache.setUpdatedAt(new DateTime());
            koodistoCacheRepository.save(cache);
            logger.debug("SAVED CACHED Koodisto items for key: "  +  key);
        } else {
            items  =  dtoConverter.convert(cache.getItems(), new ArrayList<UiKoodiItemDto>(), UiKoodiItemDto.class);
            logger.debug("Got cached results for key: "  +  key +  " updated at "  +  cache.getUpdatedAt());
        }
        memoryCache.put(key, new MemoryCacheHolder(cache.getUpdatedAt(), items));
        return items;
    }

    private boolean isCacheUsable(DateTime updatedAt) {
        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(new DateTime()) > 0;
    }

    private boolean isCacheUsed() {
        return koodistoCacheRepository != null && cacheTimeoutSeconds >= 0;
    }

    protected List<UiKoodiItemDto> findKoodistoByTyyppi(final Locale locale, final KoodistoTyyppi tyyppi) {
        return cached(new Cacheable<List<UiKoodiItemDto>>() {
            @Override
            public List<UiKoodiItemDto> get() {
                KoodistoVersioDto koodistoVersio  =  findViimeisinVoimassaOlevaKoodistonVersio(tyyppi);
                if (koodistoVersio == null) {
                    return new ArrayList<UiKoodiItemDto>(); // Palautetaan tässä
                                                            // tyhjä lista
                }
                List<KoodiDto> arvot  =  koodistoRoute.findKooditKoodistonVersiolleTyyppilla(tyyppi,
                        koodistoVersio.getVersio());
                arvot  =  filterActiveKoodis(arvot, new LocalDate());
                List<UiKoodiItemDto> optiot  =  dtoConverter.convert(arvot, new ArrayList<UiKoodiItemDto>(),
                        UiKoodiItemDto.class, locale);
                return orderNimisAsc(optiot);
            }
        }, new KoodistoCache.CacheKey(KoodistoCache.KoodistoTyyppi.valueOf(tyyppi.name()), locale));
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
    private List<KoodiDto> filterActiveKoodis(List<KoodiDto> koodit, LocalDate voimassaPvm) {
        HashMap<String, KoodiDto> aktiivisetMap  =  new HashMap<String, KoodiDto>();
        for (KoodiDto koodi : koodit) {
            if (koodi.isVoimassaPvm(voimassaPvm) && KoodistoTila.isAktiivinenTila(koodi.getTila())) {
                // Päivitetään tässä versiotieto
                String mapKoodiId  =  koodi.getKoodiArvo();
                KoodiDto aikaisempiVersio  =  aktiivisetMap.get(mapKoodiId);
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
    private List<UiKoodiItemDto> orderNimisAsc(List<UiKoodiItemDto> values) {
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
        Map<KoodistoTyyppi, List<UiKoodiItemDto>> koodistotMap  =  new HashMap<KoodistoTyyppi, List<UiKoodiItemDto>>();
        KoodistoTyyppi[] tuetutKoodistot  =  KoodistoTyyppi.values();
        for (KoodistoTyyppi tyyppi : tuetutKoodistot) {
            List<UiKoodiItemDto> koodistonArvojoukko  =  findKoodistoByTyyppi(locale, tyyppi);
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
    private KoodistoVersioDto findViimeisinVoimassaOlevaKoodistonVersio(KoodistoTyyppi tyyppi) {
        List<KoodistoVersioDto> versiot  =  koodistoRoute.findKoodistonVersiot(tyyppi);
        long maxVersionNumber  =  -1L;
        KoodistoVersioDto versioVoimassa  =  null;
        if (versiot != null && versiot.size() > 0) {
            LocalDate tanaan  =  new LocalDate();
            for (KoodistoVersioDto versio : versiot) {
                // Onko koodi aktiivinen ja tänään voimassa?
                if (versio.isVoimassaPvm(tanaan) && KoodistoTila.isAktiivinenTila(versio.getTila())) {
                    // Etsitään maksimia versionumerosta
                    if (versio.getVersio() > maxVersionNumber) {
                        maxVersionNumber  =  versio.getVersio();
                        versioVoimassa  =  versio;
                    }
                }
            }
        }
        return versioVoimassa;
    }

    public long getCacheTimeoutSeconds() {
        return cacheTimeoutSeconds;
    }

    public void setCacheTimeoutSeconds(long cacheTimeoutSeconds) {
        this.cacheTimeoutSeconds = cacheTimeoutSeconds;
    }

    public void setKoodistoRoute(KoodistoRoute koodistoRoute) {
        this.koodistoRoute  =  koodistoRoute;
    }

    public void setAuthenticationServiceRoute(AuthenticationServiceRoute authenticationServiceRoute) {
        this.authenticationServiceRoute  =  authenticationServiceRoute;
    }

    public void setOrganisaatioServiceRoute(OrganisaatioServiceRoute organisaatioServiceRoute) {
        this.organisaatioServiceRoute = organisaatioServiceRoute;
    }

}
