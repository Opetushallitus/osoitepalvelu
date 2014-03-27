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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.AndPredicateAdapter;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.HenkiloService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTermDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:22 PM
 */
@Service
@Qualifier("actual")
public class DefaultSearchService extends AbstractService implements SearchService {
    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private HenkiloService henkiloService;

    @Autowired
    private KoodistoService koodistoService;

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Override
    @Cacheable(cacheName  =  "osoitepalveluSearchResultsCache")
    public SearchResultsDto find(@PartialCacheKey SearchTermsDto terms, CamelRequestContext context) {
        SearchResultsDto results  =  new SearchResultsDto();

        boolean searchHenkilos = terms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getHenkiloTypes());
        boolean returnOrgansiaatios = terms.containsAnyTargetGroup(
                SearchTargetGroup.GroupType.getOrganisaatioPalveluTypes(), SearchTargetGroup.TargetType.ORGANISAATIO);
        boolean searchOrganisaatios = returnOrgansiaatios || searchHenkilos;

        List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults = null;
        if (searchOrganisaatios) {
            organisaatioYhteystietoResults = findOrganisaatios(terms, context);
            if (returnOrgansiaatios) {
                // Only the ones with target group and ORGANISAATIO-type selected:
                // (and apply other possible constraints):
                organisaatioYhteystietoResults = filterOrganisaatioResults(organisaatioYhteystietoResults, terms,
                        SearchTargetGroup.TargetType.ORGANISAATIO);

                // Convert to result DTOs (with e.g. postinumeros):
                List<OrganisaatioResultDto> convertedResults  =  dtoConverter.convert(
                    organisaatioYhteystietoResults, new ArrayList<OrganisaatioResultDto>(), OrganisaatioResultDto.class,
                    terms.getLocale());
                results.setOrganisaatios(convertedResults);
            } else {
                // No ORGANISAATIO types selected but possibly a number of organisaatio kind target groups present,
                // use them to filter organisaatios by which to return henkilos as well
                // (as other possible filterin constaints):
                organisaatioYhteystietoResults = filterOrganisaatioResults(organisaatioYhteystietoResults, terms);
            }
        }

        if (searchHenkilos) {
            List<String> organisaatioOids = oids(organisaatioYhteystietoResults);
            List<HenkiloDetailsDto> henkiloDetails = findHenkilos(terms, context, organisaatioOids);
            logRead(henkiloDetails);

            List<HenkiloHakuResultDto> henkiloResults = dtoConverter.convert( henkiloDetails,
                        new ArrayList<HenkiloHakuResultDto>(), HenkiloHakuResultDto.class, terms.getLocale());
            results.setHenkilos(henkiloResults);
        }

        return results;
    }

    protected List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatios(SearchTermsDto terms,
                                                       CamelRequestContext context) {
        OrganisaatioYhteystietoCriteriaDto organisaatioYhteystietosCriteria  =  new OrganisaatioYhteystietoCriteriaDto();
        List<String> kuntas  =  resolveKuntaKoodis(terms);
        organisaatioYhteystietosCriteria.setKuntaList(kuntas);
        organisaatioYhteystietosCriteria.setKieliList(terms.findTerms(SearchTermDto.TERM_ORGANISAATION_KIELIS));
        organisaatioYhteystietosCriteria.setOppilaitostyyppiList(terms.findTerms(SearchTermDto.TERM_OPPILAITOSTYYPPIS));
        organisaatioYhteystietosCriteria.setVuosiluokkaList(terms.findTerms(SearchTermDto.TERM_VUOSILUOKKAS));
        organisaatioYhteystietosCriteria.setYtunnusList(terms.findTerms(SearchTermDto.TERM_KOULTUKSENJARJESTAJAS));
        return organisaatioService.findOrganisaatioYhteystietos(organisaatioYhteystietosCriteria, context);
    }

    protected List<HenkiloDetailsDto> findHenkilos(SearchTermsDto terms, final CamelRequestContext context,
                                                List<String> organisaatioOids) {
        if (organisaatioOids.isEmpty()) {
            return new ArrayList<HenkiloDetailsDto>();
        }
        HenkiloCriteriaDto criteria = new HenkiloCriteriaDto();
        criteria.setOrganisaatioOids(organisaatioOids);
        criteria.setKayttoOikeusRayhmas(terms.findTerms(SearchTermDto.TERM_KAYTTOOIKEUSRYHMAS));
        List<HenkiloListResultDto> henkilos = henkiloService.findHenkilos(criteria, context);
        return new ArrayList<HenkiloDetailsDto>(Collections2.transform(henkilos,
                    new Function<HenkiloListResultDto, HenkiloDetailsDto>() {
            @Override
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

    protected List<OrganisaatioYhteystietoHakuResultDto> filterOrganisaatioResults(
            List<OrganisaatioYhteystietoHakuResultDto> results,
            final SearchTermsDto terms,
            SearchTargetGroup.TargetType...targetTypes) {
        AndPredicateAdapter<OrganisaatioYhteystietoHakuResultDto> predicate  =
                new AndPredicateAdapter<OrganisaatioYhteystietoHakuResultDto>();

        final List<String> organisaatioTyyppis  =  new ArrayList<String>();
        for (SearchTargetGroupDto groupDto : terms.getTargetGroups()) {
            if (groupDto.containsAnyOption(targetTypes)) {
                SearchTargetGroup.GroupType groupType  =  groupDto.getType();
                if (groupType.getOrganisaatioPalveluTyyppiArvo() != null) {
                    organisaatioTyyppis.add(groupType.getOrganisaatioPalveluTyyppiArvo());
                }
            }
        }

        if (!organisaatioTyyppis.isEmpty()) {
            predicate  =  predicate.and(new Predicate<OrganisaatioYhteystietoHakuResultDto>() {
                @Override
                public boolean apply(OrganisaatioYhteystietoHakuResultDto result) {
                    for (String tyyppi : result.getTyypit()) {
                        if (organisaatioTyyppis.contains(tyyppi)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        return new ArrayList<OrganisaatioYhteystietoHakuResultDto>(Collections2.filter(results, predicate));
    }

    protected Collection<? extends String> kuntasForMaakunta(Locale locale, String maakuntaUri) {
        return Collections2.transform(koodistoService.findKuntasByMaakuntaUri(locale, maakuntaUri),
                new Function<UiKoodiItemDto, String>() {
            @Override
            public String apply(UiKoodiItemDto koodi) {
                return koodi.getKoodiUri();
            }
        });
    }

    protected List<String> oids(List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults) {
        return new ArrayList<String>(Collections2.transform(organisaatioYhteystietoResults,
                new Function<OrganisaatioYhteystietoHakuResultDto, String>() {
            @Override
            public String apply(OrganisaatioYhteystietoHakuResultDto result) {
                return result.getOid();
            }
        }));
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
