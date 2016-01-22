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

package fi.vm.sade.osoitepalvelu.kooste.service.search;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.KoodiHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituKielisyys;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.TutkintorakenneAwareCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.AituService;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.HenkiloService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTermDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.HenkiloHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.tarjonta.TarjontaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:22 PM
 */
@Service
@Qualifier("actual")
public class DefaultSearchService extends AbstractService implements SearchService {
    private static final long serialVersionUID = -3383060564957835908L;

    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private HenkiloService henkiloService;

    @Autowired
    private KoodistoService koodistoService;

    @Autowired
    private AituService aituService;

    @Autowired
    private TarjontaService tarjontaService;


    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Override
    @Cacheable(cacheName  =  "osoitepalveluSearchResultsCache")
    public SearchResultsDto find(@PartialCacheKey SearchTermsDto terms, CamelRequestContext context)
            throws TooFewSearchConditionsForOrganisaatiosException,
                    TooFewSearchConditionsForHenkilosException,
                    TooFewSearchConditionsForKoulutusException,
                    OrganisaatioTyyppiMissingForOrganisaatiosException {

        logger.info("Starting search by {}", getLoggedInUserOidOrNull());

        SearchResultsDto results  =  new SearchResultsDto();

        boolean searchHenkilos = terms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getHenkiloHakuTypes()),
                searchToimikuntas = terms.containsAnyTargetGroup(new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT},
                        SearchTargetGroup.TargetType.JASENET,
                        SearchTargetGroup.TargetType.SIHTEERI,
                        SearchTargetGroup.TargetType.PUHEENJOHTAJA),
                searchToimikuntaEmails = terms.containsAnyTargetGroup(new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT},
                        SearchTargetGroup.TargetType.VIRANOMAIS_EMAIL),
                searchNayttotutkinnonJarjestajas = terms.containsAnyTargetGroup(
                        new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.NAYTTOTUTKINNON_JARJESTAJAT}),
                returnNayttotutkinnonJarjestajas = terms.containsAnyTargetGroup(
                        new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.NAYTTOTUTKINNON_JARJESTAJAT},
                        SearchTargetGroup.TargetType.JARJESTAJA_ORGANISAATIO,
                        SearchTargetGroup.TargetType.TUTKINTOVASTAAVA),
                returnOrgansiaatios = terms.containsAnyTargetGroup(
                        SearchTargetGroup.GroupType.getOrganisaatioPalveluTypes(), SearchTargetGroup.TargetType.ORGANISAATIO),
                searchKoulutuksenTarjoajat = terms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getKoulutusHakuTypes());

        OrganisaatioYhteystietoCriteriaDto organisaatioCriteria = produceOrganisaatioCriteria(terms);
        boolean anyOrganisaatioRelatedConditionsUsed = organisaatioCriteria.getNumberOfUsedConditions() > 0,
            searchOrganisaatios = returnOrgansiaatios || (searchHenkilos && anyOrganisaatioRelatedConditionsUsed)
                            || (searchToimikuntas && anyOrganisaatioRelatedConditionsUsed)
                            || (searchNayttotutkinnonJarjestajas && anyOrganisaatioRelatedConditionsUsed);

        List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults
                = new ArrayList<OrganisaatioYhteystietoHakuResultDto>();

        logger.info("searchKoulutuksenTarjoajat == " + searchKoulutuksenTarjoajat);

        if (searchKoulutuksenTarjoajat) {
            OrganisaatioYhteystietoCriteriaDto ktOrgCriteria = produceOrganisaatioCriteria(terms);

            // Otetaan organisaatiotyypit "KOULUTUKSEN_TARJOAJAT" optioista
            ktOrgCriteria.setOrganisaatioTyyppis(
                    parseOrganisaatioTyyppis(terms, SearchTargetGroup.GroupType.KOULUTUKSEN_TARJOAJAT));

            logger.info("Koulutuksen tarjoajat, Organisaatiotyypit: " + ktOrgCriteria.getOrganisaatioTyyppis());

            // Luodaan koulutuskriteerit
            KoulutusCriteriaDto koulutusCriteria = produceKoulutusCriteria(terms);

            List<String> oidList = tarjontaService.findOrganisaatios(koulutusCriteria, context);

            if (oidList.isEmpty()) {
                logger.info("Oid list of KOULUTUKSEN_TARJOAJA empty --> no organisations found with koulutus criteria!");
            }
            else {
                // Haetaan koulutusten tarjoajaorganisaatioiden yhteystiedot
                ktOrgCriteria.setOidList(oidList);

                List<OrganisaatioYhteystietoHakuResultDto> ktOrgYhteystietoResults;

                ktOrgYhteystietoResults = organisaatioService.findOrganisaatioYhteystietos(ktOrgCriteria,
                        terms.getLocale(), context);

                // Lisätään koulutusten tarjoajaorganisaatioiden yhteystiedot muiden organisaatioyhteystietojen joukkoon
                organisaatioYhteystietoResults.addAll(ktOrgYhteystietoResults);

                logger.info("Organisaatioiden haku kriteerit: " + ToStringBuilder.reflectionToString(ktOrgCriteria));
                logger.info("Koulutuksen tarjoajat, organisaatioiden yhteystiedot size: " + ktOrgYhteystietoResults.size());

                // Convert to result DTOs (with e.g. postinumeros):
                List<OrganisaatioResultDto> convertedResults  =  dtoConverter.convert(
                        ktOrgYhteystietoResults, new ArrayList<OrganisaatioResultDto>(),
                        OrganisaatioResultDto.class, terms.getLocale());
                results.addOrganisaatios(convertedResults);
            }
        }

        logger.info("searchOrganisaatios == " + searchOrganisaatios);

        if (searchOrganisaatios) {
            SearchTargetGroup.TargetType[] targetTypes;
            if (returnOrgansiaatios) {
                // Only the ones with target group and ORGANISAATIO-type selected:
                // (and apply other possible constraints):
                targetTypes = new SearchTargetGroup.TargetType[]{SearchTargetGroup.TargetType.ORGANISAATIO};
            } else {
                // No ORGANISAATIO types selected but possibly a number of organisaatio kind target groups present,
                // use them to filter organisaatios by which to return henkilos as well
                // (as other possible filterin constaints):
                targetTypes = new SearchTargetGroup.TargetType[0];
            }
            organisaatioCriteria.setOrganisaatioTyyppis(parseOrganisaatioTyyppis(terms, targetTypes));

            ensureAtLeastOneConditionUsed(organisaatioCriteria);

            List<OrganisaatioYhteystietoHakuResultDto> orgYhteystietoResults;

            orgYhteystietoResults = organisaatioService.findOrganisaatioYhteystietos(organisaatioCriteria,
                    terms.getLocale(), context);

            organisaatioYhteystietoResults.addAll(orgYhteystietoResults);

            if (returnOrgansiaatios) {
                // Convert to result DTOs (with e.g. postinumeros):
                List<OrganisaatioResultDto> convertedResults  =  dtoConverter.convert(
                        orgYhteystietoResults, new ArrayList<OrganisaatioResultDto>(),
                        OrganisaatioResultDto.class, terms.getLocale());
                results.addOrganisaatios(convertedResults);
            }
        }

        logger.info("searchNayttotutkinnonJarjestajas == " + searchNayttotutkinnonJarjestajas);

        if (searchNayttotutkinnonJarjestajas) {
            AituOppilaitosCriteria oppilaitosCriteria = produceOppilaitosCriteria(terms);
            if (anyOrganisaatioRelatedConditionsUsed) {
                oppilaitosCriteria.setOppilaitoskoodiIn(oppilaitoskoodis(organisaatioYhteystietoResults));
            }
            if ((!anyOrganisaatioRelatedConditionsUsed || oppilaitosCriteria.isOppilaitoskoodiUsed())
                    && returnNayttotutkinnonJarjestajas) {
                AituKielisyys orderingKielisyys = AituKielisyys.fromLocale(terms.getLocale()).or(AituKielisyys.kieli_fi);
                List<AituOppilaitosResultDto> oppilaitosResults = aituService.findNayttotutkinnonJarjestajas(
                        oppilaitosCriteria, orderingKielisyys);
                results.setAituOppilaitos(oppilaitosResults);
            } else {
                // No organisaatio rersults but organisaatio related conditions used. Should return nothing:
                results.setAituOppilaitos(new ArrayList<AituOppilaitosResultDto>());
            }
        }

        logger.info("searchToimikuntas || searchToimikuntaEmails == " + (searchToimikuntas || searchToimikuntaEmails));

        if (searchToimikuntas || searchToimikuntaEmails) {
            AituToimikuntaCriteria toimikuntaCriteria = produceToimikuntaCriteria(terms);
            if (anyOrganisaatioRelatedConditionsUsed) {
                toimikuntaCriteria.setOppilaitoskoodiIn(oppilaitoskoodis(organisaatioYhteystietoResults));
            }
            if (!anyOrganisaatioRelatedConditionsUsed || toimikuntaCriteria.isOppilaitoskoodiUsed()) {
                AituKielisyys orderingKielisyys = AituKielisyys.fromLocale(terms.getLocale()).or(AituKielisyys.kieli_fi);
                List<AituToimikuntaResultDto> toimikuntaResults = aituService.findToimikuntasWithMatchingJasens(
                        toimikuntaCriteria, orderingKielisyys);
                results.setAituToimikuntas(toimikuntaResults);
            } else {
                // No organisaatio rersults but organisaatio related conditions used. Should return nothing:
                results.setAituToimikuntas(new ArrayList<AituToimikuntaResultDto>());
            }
        }

        logger.info("searchHenkilos == " + searchHenkilos);

        if (searchHenkilos) {
            List<String> organisaatioOids = oids(organisaatioYhteystietoResults);
            List<HenkiloDetailsDto> henkiloDetails = findHenkilos(terms, context, organisaatioOids);
//            logRead(henkiloDetails);

            List<HenkiloHakuResultDto> henkiloResults = dtoConverter.convert(henkiloDetails,
                        new ArrayList<HenkiloHakuResultDto>(), HenkiloHakuResultDto.class, terms.getLocale());
            results.setHenkilos(henkiloResults);
        }

        logger.info("Search end by {}", getLoggedInUserOidOrNull());

        return results;
    }

    protected OrganisaatioYhteystietoCriteriaDto produceOrganisaatioCriteria(SearchTermsDto terms) {
        OrganisaatioYhteystietoCriteriaDto organisaatioCriteria  =  new OrganisaatioYhteystietoCriteriaDto();
        organisaatioCriteria.setKuntaList(resolveKuntaKoodis(terms));
        organisaatioCriteria.setKieliList(terms.findTerms(SearchTermDto.TERM_ORGANISAATION_KIELIS));
        organisaatioCriteria.setOppilaitostyyppiList(terms.findTerms(SearchTermDto.TERM_OPPILAITOSTYYPPIS));
        organisaatioCriteria.setVuosiluokkaList(terms.findTerms(SearchTermDto.TERM_VUOSILUOKKAS));
        organisaatioCriteria.setYtunnusList(terms.findTerms(SearchTermDto.TERM_KOULTUKSENJARJESTAJAS));
        return organisaatioCriteria;
    }

    protected void ensureAtLeastOneConditionUsed(OrganisaatioYhteystietoCriteriaDto organisaatioCriteria)
            throws TooFewSearchConditionsForOrganisaatiosException, OrganisaatioTyyppiMissingForOrganisaatiosException {
        organisaatioCriteria.setUseOrganisaatioTyyppi(true);

        logger.info("Organisaatio criteria tyypit:" + organisaatioCriteria.getOrganisaatioTyyppis());

        if (organisaatioCriteria.getOrganisaatioTyyppis().isEmpty()) {
            // If organisaatiotyyppi is missing, require it:
            throw new OrganisaatioTyyppiMissingForOrganisaatiosException();
        }
    }

    protected AituToimikuntaCriteria produceToimikuntaCriteria(SearchTermsDto terms) {
        AituToimikuntaCriteria criteria = new AituToimikuntaCriteria();
        criteria.setJasenet(terms.containsAnyTargetGroup(new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT},
                        SearchTargetGroup.TargetType.JASENET));
        criteria.setViranomaisEmail(terms.containsAnyTargetGroup(new SearchTargetGroup.GroupType[]{SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT},
                        SearchTargetGroup.TargetType.VIRANOMAIS_EMAIL));
        criteria.setKielisyysIn(terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA_KIELIS));
        criteria.setJasenKielisyysIn(terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA_JASEN_KIELIS));
        criteria.setToimikausisIn(AituToimikunta.AituToimikausi.valuesOf(
                terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA_TOIMIKAUSIS)));
        criteria.setJasensInRoolis(KoodiHelper.parseKoodiArvos(KoodistoDto.KoodistoTyyppi.TUTKINTOTOIMIKUNTA_ROOLIS.getUri(),
                terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA_ROOLIS)));
        criteria.setIdsIn(KoodiHelper.parseKoodiArvos(KoodistoDto.KoodistoTyyppi.TUTKINTOTOIMIKUNTA.getUri(),
                terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA)));
        if (criteria.isToimikausiUsed()) {
            criteria.setOnlyVoimassaOlevat(false); // -> kaikki jasenyydet
        }
        return filterTutkintorakenneRelatedConditions(criteria, terms);
    }

    protected AituOppilaitosCriteria produceOppilaitosCriteria(SearchTermsDto terms) {
        AituOppilaitosCriteria criteria = new AituOppilaitosCriteria();
        criteria.setToimikuntaIn(KoodiHelper.parseKoodiArvos(KoodistoDto.KoodistoTyyppi.TUTKINTOTOIMIKUNTA.getUri(),
                terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA)));
        criteria.setToimikausisIn(AituToimikunta.AituToimikausi.valuesOf(
                terms.findTerms(SearchTermDto.TERM_TUTKINTOIMIKUNTA_TOIMIKAUSIS)));

        return filterTutkintorakenneRelatedConditions(criteria, terms);
    }

    protected KoulutusCriteriaDto produceKoulutusCriteria(SearchTermsDto terms) {
        KoulutusCriteriaDto criteria = new KoulutusCriteriaDto();

        // Koulutuslaji --> There can be only one koulutuslaji
        if (terms.findTerms(SearchTermDto.TERM_KOULUTUSLAJIS).size() == 1 &&
                terms.findTerms(SearchTermDto.TERM_KOULUTUSLAJIS).get(0).isEmpty() == false) {
            criteria.setKoulutuslaji(terms.findTerms(SearchTermDto.TERM_KOULUTUSLAJIS).get(0));
        }
        if (terms.findTerms(SearchTermDto.TERM_KOULUTUSLAJIS).size() > 1) {
            logger.error("Too many terms for koulutuslaji, only one can be defined!");
        }

        // Opetuskieli
        criteria.setOpetuskielet(terms.findTerms(SearchTermDto.TERM_OPETUSKIELIS));

        // Koulutustyyppi
        criteria.setKoulutustyyppis(terms.findTerms(SearchTermDto.TERM_KOULUTUSTYYPPIS));

        // Koulutusala
        criteria.setKoulutusalakoodis(terms.findTerms(SearchTermDto.TERM_KOULUTUSALAS));

        // Opintoala
        criteria.setOpintoalakoodis(terms.findTerms(SearchTermDto.TERM_OPINTOALAS));

        // Koulutus
        criteria.setKoulutuskoodis(terms.findTerms(SearchTermDto.TERM_KOULUTUS));

        return criteria;
    }

    protected <C extends TutkintorakenneAwareCriteria> C filterTutkintorakenneRelatedConditions(C criteria,
                                                                                                SearchTermsDto terms) {
        criteria.setTutkintoTunnusIn(KoodiHelper.parseKoodiArvos(KoodistoDto.KoodistoTyyppi.KOULUTUS.getUri(),
                terms.findTerms(SearchTermDto.TERM_KOULUTUS)));
        criteria.setOpintoalaTunnusIn(KoodiHelper.parseKoodiArvos(KoodistoDto.KoodistoTyyppi.OPINTOALAOPH2002.getUri(),
                terms.findTerms(SearchTermDto.TERM_OPINTOALAS)));
        boolean tutkintosWasUsed = criteria.isTutkintoUsed();
        if (!tutkintosWasUsed) {
            // If koulutus tyyppi is used, include all koulutus by tyyppi. In other cases, will be used to limit
            // koulutus options (and those are more specific if given)
            List<String> koulutustyyppis = terms.findTerms(SearchTermDto.TERM_KOULUTUSTYYPPIS);
            if (koulutustyyppis != null && !koulutustyyppis.isEmpty()) {
                criteria.setTutkintoTunnusIn(new ArrayList<String>(Collections2.transform(koodistoService
                        .findKoulutusByOpintoalasOrTyyppis(terms.getLocale(), null,
                                koulutustyyppis.toArray(new String[0])),
                        UiKoodiItemDto.KOODI_ID)));
            }
        }
        if (!tutkintosWasUsed && !criteria.isOpintoalaUsed()) {
            // If opintoala or tutkinto conditions used, they are (or are like to be) selected through koulutusala.
            // However, if only koulutusalas selected, selecting the opintoalas for the selected koulutusalas
            List<String> koulutusalas = terms.findTerms(SearchTermDto.TERM_KOULUTUSALAS);
            if (koulutusalas != null && !koulutusalas.isEmpty()) {
                criteria.setOpintoalaTunnusIn(new ArrayList<String>(Collections2.transform(koodistoService
                        .findOpintoAlasByKoulutusAlas(terms.getLocale(), koulutusalas.toArray(new String[0])),
                        UiKoodiItemDto.KOODI_ID)));
            }
        }

        return criteria;
    }

    protected List<String> parseOrganisaatioTyyppis(SearchTermsDto terms, SearchTargetGroup.GroupType groupType) {
        List<String> organisaatioTyyppis  =  new ArrayList<String>();
        SearchTargetGroupDto groupDto = terms.getTargetGroup(groupType);

        if (groupDto == null) {
            return organisaatioTyyppis;
        }

        for (SearchTargetGroup.TargetType option : groupDto.getOptions()) {
            if (Arrays.asList(SearchTargetGroup.TargetType.getOrganisaatioPalveluTypes()).contains(option)) {
                organisaatioTyyppis.addAll(Arrays.asList(option.getOrganisaatioPalveluTyyppiArvo()));
            }
        }
        return organisaatioTyyppis;
    }

    protected List<String> parseOrganisaatioTyyppis(SearchTermsDto terms, SearchTargetGroup.TargetType... targetTypes) {
        List<String> organisaatioTyyppis  =  new ArrayList<String>();
        for (SearchTargetGroupDto groupDto : terms.getTargetGroups()) {
            if (groupDto.containsAnyOption(targetTypes)) {
                SearchTargetGroup.GroupType groupType  =  groupDto.getType();
                if (groupType.getOrganisaatioPalveluTyyppiArvo() != null) {
                    organisaatioTyyppis.addAll(Arrays.asList(groupType.getOrganisaatioPalveluTyyppiArvo()));
                }
            }
        }
        return organisaatioTyyppis;
    }

    protected List<HenkiloDetailsDto> findHenkilos(SearchTermsDto terms, final CamelRequestContext context,
                                                List<String> organisaatioOids) throws TooFewSearchConditionsForHenkilosException {
        HenkiloCriteriaDto criteria = new HenkiloCriteriaDto();
        criteria.setOrganisaatioOids(organisaatioOids);
        criteria.setKayttoOikeusRayhmas(terms.findTerms(SearchTermDto.TERM_KAYTTOOIKEUSRYHMAS));

        if (criteria.getNumberOfUsedConditions() < 1) {
            throw new TooFewSearchConditionsForHenkilosException();
        }

        List<HenkiloListResultDto> henkilos = henkiloService.findHenkilos(criteria, context);
        return new ArrayList<HenkiloDetailsDto>(Collections2.transform(henkilos,
                    new Function<HenkiloListResultDto, HenkiloDetailsDto>() {
            public HenkiloDetailsDto apply(HenkiloListResultDto result) {
                return henkiloService.getHenkiloTiedot(result.getOidHenkilo(), context);
            }
        }));
    }

    protected void logRead(List<HenkiloDetailsDto> henkiloDetailsList) {
        for (HenkiloDetailsDto henkiloDetails : henkiloDetailsList) {
            log(read("henkilo", henkiloDetails.getOidHenkilo()));
        }
    }

    protected List<String> resolveKuntaKoodis(SearchTermsDto terms) {
        List<String> kuntas  =  new ArrayList<String>();
        kuntas.addAll(terms.findTerms(SearchTermDto.TERM_KUNTAS));
        for(String maakuntaKoodi : terms.findTerms(SearchTermDto.TERM_MAAKUNTAS)) {
            kuntas.addAll(kuntasForMaakunta(terms.getLocale(), maakuntaKoodi));
        }
        return kuntas;
    }

    protected Collection<? extends String> kuntasForMaakunta(Locale locale, String maakuntaUri) {
        return Collections2.transform(koodistoService.findKuntasByMaakuntaUri(locale, maakuntaUri),
                new Function<UiKoodiItemDto, String>() {
            public String apply(UiKoodiItemDto koodi) {
                return koodi.getKoodiUri();
            }
        });
    }

    protected List<String> oids(List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults) {
        return new ArrayList<String>(Collections2.transform(organisaatioYhteystietoResults,
                FilterableOrganisaatio.GET_OID));
    }

    protected List<String> oppilaitoskoodis(List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults) {
        List<String> koodis = new ArrayList<String>();
        for (OrganisaatioYhteystietoHakuResultDto result : organisaatioYhteystietoResults) {
            if (result.getOppilaitosKoodi() != null) {
                koodis.add(result.getOppilaitosKoodi());
            }
        }
        return koodis;
    }

    public void setTarjontaService(TarjontaService tarjontaService) {
        this.tarjontaService = tarjontaService;
    }

    public void setOrganisaatioService(OrganisaatioService organisaatioService) {
        this.organisaatioService = organisaatioService;
    }

    public void setHenkiloService(HenkiloService henkiloService) {
        this.henkiloService = henkiloService;
    }

    public void setKoodistoService(KoodistoService koodistoService) {
        this.koodistoService  =  koodistoService;
    }

    public void setAituService(AituService aituService) {
        this.aituService = aituService;
    }

}
