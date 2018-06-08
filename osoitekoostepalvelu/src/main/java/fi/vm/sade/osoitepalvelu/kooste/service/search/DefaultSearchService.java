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
import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.auditlog.osoitepalvelu.OsoitepalveluOperation;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
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

import static fi.vm.sade.auditlog.osoitepalvelu.LogMessage.builder;
import fi.vm.sade.auditlog.osoitepalvelu.LogMessage;

import java.util.*;
import org.apache.commons.lang.builder.ToStringBuilder;

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
    private TarjontaService tarjontaService;

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Autowired
    private Audit audit;

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
                returnOrgansiaatios = terms.containsAnyTargetGroup(
                        SearchTargetGroup.GroupType.getOrganisaatioPalveluTypes(), SearchTargetGroup.TargetType.ORGANISAATIO),
                searchKoulutuksenTarjoajat = terms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getKoulutusHakuTypes());

        OrganisaatioYhteystietoCriteriaDto organisaatioCriteria = produceOrganisaatioCriteria(terms);
        boolean anyOrganisaatioRelatedConditionsUsed = organisaatioCriteria.getNumberOfUsedConditions() > 0,
            searchOrganisaatios = returnOrgansiaatios || (searchHenkilos && anyOrganisaatioRelatedConditionsUsed);

        List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults
                = new ArrayList<OrganisaatioYhteystietoHakuResultDto>();

        logger.debug("searchKoulutuksenTarjoajat == " + searchKoulutuksenTarjoajat);

        if (searchKoulutuksenTarjoajat) {
            OrganisaatioYhteystietoCriteriaDto ktOrgCriteria = produceOrganisaatioCriteria(terms);

            // Otetaan organisaatiotyypit "KOULUTUKSEN_TARJOAJAT" optioista
            ktOrgCriteria.setOrganisaatioTyyppis(
                    parseOrganisaatioTyyppis(terms, SearchTargetGroup.GroupType.KOULUTUKSEN_TARJOAJAT));

            logger.debug("Koulutuksen tarjoajat, Organisaatiotyypit: " + ktOrgCriteria.getOrganisaatioTyyppis());

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

                logger.debug("Organisaatioiden haku kriteerit: " + ToStringBuilder.reflectionToString(ktOrgCriteria));
                logger.debug("Koulutuksen tarjoajat, organisaatioiden yhteystiedot size: " + ktOrgYhteystietoResults.size());

                // Convert to result DTOs (with e.g. postinumeros):
                List<OrganisaatioResultDto> convertedResults  =  dtoConverter.convert(
                        ktOrgYhteystietoResults, new ArrayList<OrganisaatioResultDto>(),
                        OrganisaatioResultDto.class, terms.getLocale());
                results.addOrganisaatios(convertedResults);
            }
        }

        logger.debug("searchOrganisaatios == " + searchOrganisaatios);

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

            // When searching henkilös we don't want to limit the search.
            if(!searchHenkilos) {
                ensureAtLeastOneConditionUsed(organisaatioCriteria);
            }

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

        logger.debug("searchHenkilos == " + searchHenkilos);

        if (searchHenkilos) {
            List<String> organisaatioOids = oids(organisaatioYhteystietoResults);
            List<HenkiloDetailsDto> henkiloDetails = findHenkilos(terms, context, organisaatioOids);
            // Searching individuals may contain sensitive content so it is logged.
            logRead(henkiloDetails);

            List<HenkiloHakuResultDto> henkiloResults = dtoConverter.convert(henkiloDetails,
                        new ArrayList<HenkiloHakuResultDto>(), HenkiloHakuResultDto.class, terms.getLocale());
            results.setHenkilos(henkiloResults);
        }

        logger.debug("Search end by {}", getLoggedInUserOidOrNull());

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

        return henkiloService.findHenkilos(criteria, context);
    }

    // Combine fetched henkiloOids into single string and log what was fetched.
    protected void logRead(List<HenkiloDetailsDto> henkiloDetailsList) {

        String henkiloOids = "";
        for (HenkiloDetailsDto henkiloDetails : henkiloDetailsList) {
            henkiloOids += henkiloDetails.getOidHenkilo() + ";";
        }
        LogMessage logMessage = builder().id(getLoggedInUserOidOrNull()).henkiloOidList(henkiloOids)
                .setOperaatio(OsoitepalveluOperation.HENKILO_HAKU).build();
        audit.log(logMessage);
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
        List<String> oidList = new ArrayList<String>(Collections2.transform(organisaatioYhteystietoResults,
                FilterableOrganisaatio.GET_OID));
        // Remove opetushallitus since it's included as root organisation of koulutustoimijas.
        oidList.remove("1.2.246.562.10.00000000001");
        return oidList;
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

}
