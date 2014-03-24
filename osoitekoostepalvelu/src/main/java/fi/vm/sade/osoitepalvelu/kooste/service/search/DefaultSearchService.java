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
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioTiedotDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
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
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired
    private KoodistoService koodistoService;

    @Autowired
    private SearchResultDtoConverter searchResultDtoConverter;

    @Override
    @Cacheable(cacheName = "osoitepalveluSearchResultsCache")
    public OrganisaatioResultsDto find(@PartialCacheKey SearchTermsDto terms, CamelRequestContext context) {
        OrganisaatioResultsDto results = new OrganisaatioResultsDto();

        if (terms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getOrganisaatioPalveluTypes())) {
            List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults
                    = findOrganisaatios(terms, context);
            List<OrganisaatioTiedotDto> convertedResults = searchResultDtoConverter.convert(
                    organisaatioYhteystietoResults, new ArrayList<OrganisaatioTiedotDto>(), OrganisaatioTiedotDto.class,
                    terms.getLocale());
            results.getResults().addAll(convertedResults);
        }

        // TOOD: hae henkiloöt

        //log(read("henkilo", "1.2.3.4.238726766"));

        return results;
    }

    protected List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatios(SearchTermsDto terms,
                                                                           CamelRequestContext context) {
        OrganisaatioYhteystietoCriteriaDto organisaatioYhteystietosCriteria = new OrganisaatioYhteystietoCriteriaDto();
        List<String> kuntas = resolveKuntaKoodis(terms);
        organisaatioYhteystietosCriteria.setKuntaList( kuntas );
        organisaatioYhteystietosCriteria.setKieliList(terms.findTerms("organisaationKielis"));
        organisaatioYhteystietosCriteria.setOppilaitostyyppiList(terms.findTerms("oppilaitostyyppis"));
        organisaatioYhteystietosCriteria.setVuosiluokkaList(terms.findTerms("vuosiluokkas"));
        /// TODO: koultuksenjarjestajas,
        List<OrganisaatioYhteystietoHakuResultDto> results
                = organisaatioServiceRoute.findOrganisaatioYhteystietos(organisaatioYhteystietosCriteria, context);
        return filterOrganisaatioResults(results, terms);
    }

    protected List<String> resolveKuntaKoodis(SearchTermsDto terms) {
        List<String> kuntas = new ArrayList<String>();
        kuntas.addAll(terms.findTerms("kuntas"));
        for( String maakuntaKoodi : terms.findTerms("maakuntas") ) {
            kuntas.addAll( kuntasForMaakunta(terms.getLocale(), maakuntaKoodi) );
        }
        return kuntas;
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

    protected List<OrganisaatioYhteystietoHakuResultDto> filterOrganisaatioResults(List<OrganisaatioYhteystietoHakuResultDto> results,
                                                                                 final SearchTermsDto terms) {
        AndPredicateAdapter<OrganisaatioYhteystietoHakuResultDto> predicate =
                new AndPredicateAdapter<OrganisaatioYhteystietoHakuResultDto>();

        final List<String> organisaatioTyyppis = new ArrayList<String>();
        for (SearchTargetGroupDto groupDto : terms.getTargetGroups()) {
            SearchTargetGroup.GroupType groupType = groupDto.getType();
            if (groupType.getOrganisaatioPalveluTyyppiArvo() != null) {
                organisaatioTyyppis.add(groupType.getOrganisaatioPalveluTyyppiArvo());
            }
        }
        predicate = predicate.and(new Predicate<OrganisaatioYhteystietoHakuResultDto>() {
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

        return new ArrayList<OrganisaatioYhteystietoHakuResultDto>(Collections2.filter(results, predicate));
    }

    public void setOrganisaatioServiceRoute(OrganisaatioServiceRoute organisaatioServiceRoute) {
        this.organisaatioServiceRoute = organisaatioServiceRoute;
    }

    public void setKoodistoService(KoodistoService koodistoService) {
        this.koodistoService = koodistoService;
    }
}
