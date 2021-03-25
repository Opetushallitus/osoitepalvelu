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
import com.google.common.collect.Ordering;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.Combiner;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier.*;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto.SearchType;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DefaultSearchResultTransformerService extends AbstractService
        implements SearchResultTransformerService {

    private static final long serialVersionUID = -5456950030215538503L;

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Autowired
    private MessageService messageService;

    @Autowired(required = false)
    private OrganisaatioService organisaatioService;

    @Override
    public SearchResultsPresentationDto transformToResultRows(SearchResultsDto results,
                                                              final SearchResultPresentation presentation, CamelRequestContext context, SearchType searchType) {
        Set<SourceRegister> sourceRegisters = new HashSet<>();

        List<SearchResultRowDto> organisaatioResults = transformOrganisaatios(results.getOrganisaatios(), presentation);
        if (!organisaatioResults.isEmpty()) {
            sourceRegisters.add(SourceRegister.opintopolku);
        }
        List<SearchResultRowDto> transformedResults = new ArrayList<>(organisaatioResults);

        List<SearchResultRowDto> henkiloResults = transformHenkilos(results.getHenkilos());
        if (!henkiloResults.isEmpty()) {
            sourceRegisters.add(SourceRegister.opintopolku);
            // Can not distinguish results originating from Aipal wihtin the results from henkilopalvelu:
            sourceRegisters.add(SourceRegister.aipal);
        }
        transformedResults.addAll(henkiloResults);

        resolveMissingOrganisaatioRelatedDetails(transformedResults, presentation, context);

        // OVT-8440: first remove duplicates, then delete rows:
        List<SearchResultRowDto> rows = removeDuplicates(searchType, presentation, transformedResults);
        int rownum = 1;
        for (SearchResultRowDto row : rows) {
            row.setRivinumero(rownum++);
        }
        rows = removeDeletedRows(presentation, rows);

        // Mix the organisation and henkilo based rows by organisaatio's oid
        rows = applyOrdering(rows);

        SearchResultsPresentationDto presentationDto = new SearchResultsPresentationDto(rows, presentation);
        presentationDto.setSourceRegisters(sourceRegisters);
        return presentationDto;
    }

    protected List<SearchResultRowDto> removeDuplicates(SearchType searchType, final SearchResultPresentation presentation,
                                                        List<SearchResultRowDto> rows) {
        if (searchType == SearchType.EMAIL) {
            // Kyseessä email-tyyppinen haku, joten nyt suodatetaan kaikki dublikaatti-emailit pois.
            Set<String> emails = new TreeSet<>();
            List<SearchResultRowDto> filtteredTransformedResults = new ArrayList<>();

            if (presentation.isOrganisaatioEmailOnlyEmailIncluded()) {
                for (SearchResultRowDto dto : rows) {
                    if (dto.getEmailOsoite() != null && !emails.contains(dto.getEmailOsoite())) {
                        emails.add(dto.getEmailOsoite());
                        filtteredTransformedResults.add(dto);
                    }
                }
            } else {
                for (SearchResultRowDto dto : rows) {
                    if (dto.getHenkiloEmail() != null && !emails.contains(dto.getHenkiloEmail())) {
                        emails.add(dto.getHenkiloEmail());
                        filtteredTransformedResults.add(dto);
                    } else if (dto.getEmailOsoite() != null && !emails.contains(dto.getEmailOsoite())) {
                        emails.add(dto.getEmailOsoite());
                        filtteredTransformedResults.add(dto);
                    } else if (dto.getKoulutusneuvonnanEmail() != null && !emails.contains(dto.getKoulutusneuvonnanEmail())) {
                        emails.add(dto.getKoulutusneuvonnanEmail());
                        filtteredTransformedResults.add(dto);
                    } else if (dto.getKriisitiedotuksenEmail() != null && !emails.contains(dto.getKriisitiedotuksenEmail())) {
                        emails.add(dto.getKriisitiedotuksenEmail());
                        filtteredTransformedResults.add(dto);
                    } else if (dto.getViranomaistiedotuksenEmail() != null && !emails.contains(
                            dto.getViranomaistiedotuksenEmail())) {
                        emails.add(dto.getViranomaistiedotuksenEmail());
                        filtteredTransformedResults.add(dto);
                    }
                }
            }
            // Asetetaan tulosjoukoksi filtteröity listaus.
            rows = filtteredTransformedResults;
        } else {
            // Poistetaan selkeät duplikaatit (joita tulee esim. näyttötutkinnon järjestäjien eri tutkintojen vastaavista
            // henkilöistä, joita ei voida ):
            rows = removeDuplicatesByUniqueState(rows);
        }
        return rows;
    }

    protected List<SearchResultRowDto> removeDeletedRows(final SearchResultPresentation presentation,
                                                         List<SearchResultRowDto> rows) {
        return new ArrayList<>(Collections2.filter(rows, presentation::isResultRowIncluded));
    }

    protected List<SearchResultRowDto> removeDuplicatesByUniqueState(List<SearchResultRowDto> rows) {
        final Set<EqualsHelper> states = new HashSet<>();
        return new ArrayList<>(Collections2.filter(rows, input -> states.add(input.uniqueState())));
    }

    protected List<SearchResultRowDto> applyOrdering(List<SearchResultRowDto> transformedResults) {
        //
        return Ordering
                .natural().nullsLast()
                .onResultOf((Function<SearchResultRowDto, String>) row -> row.getNimi())
                .compound(Ordering.natural().nullsFirst()
                        .onResultOf((Function<SearchResultRowDto, String>) row -> row.getOrganisaatioOid()))
                .compound(Ordering.natural().nullsFirst()
                        .onResultOf((Function<SearchResultRowDto, String>) row -> row.getYhteystietoNimi()))
                .compound(Ordering.natural().nullsFirst()
                        .onResultOf((Function<SearchResultRowDto, String>) row -> row.getHenkiloOid())).sortedCopy(transformedResults);
    }

    /**
     * Creates a scalar product of given orgranisaatios, their yhteyshenkilos and postiosoites.
     * Filters the postiosoites to match the possibly given locale of the presentation and localizes orgnisaatio's
     * nimi with the same locale.
     *
     * @param results      the organisaatios to transform
     * @param presentation the desired presentation
     * @return the tranformed search result rows for organsiaatios
     */
    protected List<SearchResultRowDto> transformOrganisaatios(List<OrganisaatioResultDto> results,
                                                              SearchResultPresentation presentation) {
        List<SearchResultRowDto> transformedResults = new ArrayList<>();

        Set<OrganisaatioResultAggregateDto> organisaatioAggregates = new LinkedHashSet<>();
        for (final OrganisaatioResultDto result : results) {
            Combiner<OrganisaatioResultAggregateDto> combiner = new Combiner<>(
                    src -> new OrganisaatioResultAggregateDto(result, src.get(OrganisaatioYhteystietoDto.class).orNull(),
                            src.get(OsoitteistoDto.class).orNull(),
                            src.get(KayntiosoitteistoDto.class).orNull()));
            if (presentation.isPositosoiteIncluded()) {
                combiner.combinedWith(OsoitteistoDto.class,
                        filterOsoites(result.getPostiosoite(), presentation.getLocale()));
            }
            if (presentation.isKayntiosoiteIncluded()) {
                combiner.combinedWith(KayntiosoitteistoDto.class,
                        filterKayntiosoites(result.getKayntiosoite(), presentation.getLocale()));
            }
            if (presentation.isYhteyshenkiloIncluded() || presentation.isYhteyshenkiloEmailIncluded()) {
                combiner.combinedWith(OrganisaatioYhteystietoDto.class, result.getYhteyshenkilot());
            }
            combiner.atLeastOne().to(organisaatioAggregates);
        }
        for (OrganisaatioResultAggregateDto aggregate : organisaatioAggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            row.setNimi(LocaleHelper.localized(aggregate.getOrganisaatio().getNimi(), presentation.getLocale(), DEFAULT_LOCALE));
            transformedResults.add(row);
        }
        return transformedResults;
    }

    protected List<KayntiosoitteistoDto> filterKayntiosoites(List<KayntiosoitteistoDto> osoites, Locale locale) {
        return filterOsoites(osoites, locale);
    }

    protected <T extends OsoitteistoDto> List<T> filterOsoites(List<T> osoites, Locale locale) {
        if (locale == null || osoites.size() < 1) {
            return osoites;
        }
        List<T> filtered = new ArrayList<>();
        for (T osoite : osoites) {
            if (LocaleHelper.languageEquals(locale, LocaleHelper.parseLocale(osoite.getKieli(), DEFAULT_LOCALE))) {
                filtered.add(osoite);
            }
        }
        if (filtered.isEmpty() && !LocaleHelper.languageEquals(DEFAULT_LOCALE, locale)) {
            filtered = filterOsoites(osoites, DEFAULT_LOCALE);
        }
        if (filtered.isEmpty() && !osoites.isEmpty()) {
            // Add any over nothing:
            filtered.add(osoites.iterator().next());
        }
        return filtered;
    }

    protected List<SearchResultRowDto> transformHenkilos(List<HenkiloHakuResultDto> henkilos) {
        List<SearchResultRowDto> results = new ArrayList<>();

        Set<HenkiloResultAggregateDto> henkiloAggregates = new LinkedHashSet<>();
        for (final HenkiloHakuResultDto result : henkilos) {
            new Combiner<>(
                    src -> new HenkiloResultAggregateDto(result,
                            src.get(HenkiloOsoiteDto.class).orNull()))
                    .withRepeated(HenkiloOsoiteDto.class, result.getOsoittees())
                    .atLeastOne().to(henkiloAggregates);
        }
        for (HenkiloResultAggregateDto aggregate : henkiloAggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            results.add(row);
        }
        return results;
    }

    // Create custom copiers for result details. If for some strange reason a detail is missing from a row the info is
    // fetched and the this copier is used to get the detail.
    protected void resolveMissingOrganisaatioRelatedDetails(List<SearchResultRowDto> results,
                                                            SearchResultPresentation presentation, CamelRequestContext context) {
        if (organisaatioService == null) {
            return;
        }
        List<DetailCopier> copiers = new ArrayList<>();

        if (presentation.isOrganisaationNimiIncluded()) {
            copiers.add(new OrganisaationNimiCopier());
        }

        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            copiers.add(new OrganisaationSijaintikuntaCopier());
        }

        if (presentation.isOrganisaatiotunnisteIncluded()) {
            copiers.add(new OrganisaatiotunnisteCopier());
        }

        if (presentation.isWwwOsoiteIncluded()) {
            copiers.add(new WwwOsoiteCopier());
        }

        if (presentation.isOrganisaatioEmailIncluded()) {
            copiers.add(new OrganisaatioEmailCopier());
        }

        if (presentation.isPuhelinnumeroIncluded()) {
            copiers.add(new PuhelinnumeroCopier());
        }

        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            copiers.add(new KriisitiedotuksenSahkopostiosoiteCopier());
        }

        if (presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            copiers.add(new VarhaiskasvatuksenYhteyshenkiloCopier());
        }

        if (presentation.isVarhaiskasvatuksenEmailIncluded()) {
            copiers.add(new VarhaiskasvatuksenEmailCopier());
        }

        if (presentation.isKoskiYhdyshenkiloIncluded()) {
            copiers.add(new KoskiYhdyshenkiloCopier());
        }

        if (presentation.isMoveYhteyshenkiloIncluded()) {
            copiers.add(new MoveYhteyshenkiloCopier());
        }

        // TODO: viranomaistiedotuksenEmail, koulutusneuvonnanEmail
        copyDetails(results, context, copiers, presentation.getLocale());
    }

    protected void copyDetails(List<SearchResultRowDto> results, CamelRequestContext context,
                               List<DetailCopier> copiers, Locale locale) {
        Map<String, OrganisaatioDetailsDto> cache = new HashMap<>();

        if (copiers.isEmpty()) {
            return;
        }

        try {
            Map<String, String> oidsByOppilaitosKoodi = new HashMap<>();
            // Go through each row and use all included copiers to check content. if content is missing add the details to the result.
            results:
            for (SearchResultRowDto result : results) {
                String oid = result.getOrganisaatioOid();
                if (oid == null && result.getOppilaitosKoodi() != null) {
                    oid = oidsByOppilaitosKoodi.get(result.getOppilaitosKoodi());
                    if (oid == null) {
                        try {
                            oid = organisaatioService.findOidByOppilaitoskoodi(result.getOppilaitosKoodi());
                        } catch (Exception e) {
                            result.setNimi(this.messageService.getMessage("organisaatio_oid_not_found_by_oppilaitoskoodi", locale, result.getOppilaitosKoodi()));
                            continue results;
                        }
                        oidsByOppilaitosKoodi.put(result.getOppilaitosKoodi(), oid);
                    }
                }
                if (oid != null) {
                    for (DetailCopier copier : copiers) {
                        if (copier.isMissing(result)) {
                            OrganisaatioDetailsDto details;
                            if (cache.containsKey(oid)) {
                                details = cache.get(oid);
                            } else {
                                long rc = context.getRequestCount();
                                try {
                                    details = organisaatioService.getdOrganisaatioByOid(oid, context);
                                } catch (Exception e) {
                                    result.setNimi(this.messageService.getMessage("orgnisaatio_not_found_by_oid", locale, oid));
                                    continue results;
                                }
                                cache.put(oid, details);
                            }
                            copier.copy(details, result, locale);
                        }
                    }

                }
            }
        } finally {
        }
    }

    @Override
    public String getLoggeInUserOid() {
        return super.getLoggedInUserOid();
    }

    public void setOrganisaatioService(OrganisaatioService organisaatioService) {
        this.organisaatioService = organisaatioService;
    }
}
