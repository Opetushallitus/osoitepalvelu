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

import static fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper.join;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;

import fi.vm.sade.javautils.poi.OphCellStyles;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.Combiner;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituJasenyysDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOppilaitosResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituSopimusDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituToimikuntaResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituTutkintoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.*;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.AituOppilaitosVastuuhenkiloAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.AituToimikuntaJasenAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.HenkiloHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.HenkiloOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.HenkiloResultAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto.SearchType;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SourceRegister;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class DefaultSearchResultTransformerService extends AbstractService
        implements SearchResultTransformerService {

    private static final long serialVersionUID = -5456950030215538503L;

    private static final Locale DEFAULT_LOCALE = new Locale("fi", "FI");

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Autowired
    private MessageSource messageSource;

    @Autowired(required = false)
    private OrganisaatioService organisaatioService;

    @Autowired
    private KoodistoService koodistoService;

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

        List<SearchResultRowDto> aituHenkiloResults = transformToimikuntaJasens(results.getAituToimikuntas(), presentation);
        if (!aituHenkiloResults.isEmpty()) {
            sourceRegisters.add(SourceRegister.aitu);
        }
        transformedResults.addAll(aituHenkiloResults);

        List<SearchResultRowDto> aituOppilaitosResults = transformNayttotutkinonJarjestajaOrganisaatios(results
                .getAituOppilaitos(), presentation);
        if (!aituOppilaitosResults.isEmpty()) {
            sourceRegisters.add(SourceRegister.aitu);
            // The actual data about the oppilaitos organisaatios may originate from organisaatio-service:
            sourceRegisters.add(SourceRegister.opintopolku);
        }
        transformedResults.addAll(aituOppilaitosResults);

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
        if(searchType == SearchType.EMAIL) {
            // Kyseessä email-tyyppinen haku, joten nyt suodatetaan kaikki dublikaatti-emailit pois.
            Set<String> emails = new TreeSet<>();
            List<SearchResultRowDto> filtteredTransformedResults = new ArrayList<>();

            if (presentation.isOrganisaatioEmailOnlyEmailIncluded()) {
                for (SearchResultRowDto dto : rows) {
                    if(dto.getEmailOsoite() != null && !emails.contains(dto.getEmailOsoite())) {
                        emails.add(dto.getEmailOsoite());
                        filtteredTransformedResults.add(dto);
                    }
                }
            } else {
                for (SearchResultRowDto dto : rows) {
                    if(dto.getHenkiloEmail() != null && !emails.contains(dto.getHenkiloEmail())) {
                        emails.add(dto.getHenkiloEmail());
                        filtteredTransformedResults.add(dto);
                    } else if(dto.getEmailOsoite() != null && !emails.contains(dto.getEmailOsoite())) {
                        emails.add(dto.getEmailOsoite());
                        filtteredTransformedResults.add(dto);
                    } else if(dto.getKoulutusneuvonnanEmail() != null && !emails.contains(dto.getKoulutusneuvonnanEmail())) {
                        emails.add(dto.getKoulutusneuvonnanEmail());
                        filtteredTransformedResults.add(dto);
                    } else if(dto.getKriisitiedotuksenEmail() != null && !emails.contains(dto.getKriisitiedotuksenEmail())) {
                        emails.add(dto.getKriisitiedotuksenEmail());
                        filtteredTransformedResults.add(dto);
                    } else if(dto.getViranomaistiedotuksenEmail() != null && !emails.contains(
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
     *Creates a scalar product of given orgranisaatios, their yhteyshenkilos and postiosoites.
     * Filters the postiosoites to match the possibly given locale of the presentation and localizes orgnisaatio's
     * nimi with the same locale.
     *
     * @param results the organisaatios to transform
     * @param presentation the desired presentation
     * @return the tranformed search result rows for organsiaatios
     */
    protected List<SearchResultRowDto> transformOrganisaatios(List<OrganisaatioResultDto> results,
                                        SearchResultPresentation presentation) {
        List<SearchResultRowDto> transformedResults = new ArrayList<>();

        Set<OrganisaatioResultAggregateDto> organisaatioAggregates = new LinkedHashSet<>();
        for (final OrganisaatioResultDto result : results) {
            Combiner<OrganisaatioResultAggregateDto> combiner = new Combiner<>(
                src -> new OrganisaatioResultAggregateDto(result,
                    src.get(OrganisaatioYhteystietoDto.class).orNull(),
                    src.get(OsoitteistoDto.class).orNull(),
                    src.get(OsoitteistoDto.class).orNull()));
            if (presentation.isPositosoiteIncluded()) {
                combiner.combinedWith(OsoitteistoDto.class,
                        filterOsoites(result.getPostiosoite(), presentation.getLocale()));
                if (presentation.isKayntiosoiteIncluded()) {
                    combiner.withRepeated(OsoitteistoDto.class,
                            filterOsoites(result.getKayntiosoite(), presentation.getLocale()));
                }
            } else if (presentation.isKayntiosoiteIncluded()) {
                combiner.with(OsoitteistoDto.class, new ArrayList<>())
                    .combinedWith(OsoitteistoDto.class,
                        filterOsoites(result.getKayntiosoite(), presentation.getLocale()));
            }
            if (presentation.isYhteyshenkiloIncluded() || presentation.isYhteyshenkiloEmailIncluded()) {
                combiner.combinedWith(OrganisaatioYhteystietoDto.class, result.getYhteyshenkilot());
            }
            combiner.atLeastOne().to(organisaatioAggregates);
        }
        for (OrganisaatioResultAggregateDto aggregate : organisaatioAggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            row.setNimi(localized(aggregate.getOrganisaatio().getNimi(), presentation.getLocale(), DEFAULT_LOCALE));
            transformedResults.add(row);
        }
        return transformedResults;
    }

    protected List<OsoitteistoDto> filterOsoites(List<OsoitteistoDto> osoites, Locale locale) {
        if (locale == null || osoites.size() < 1) {
            return osoites;
        }
        List<OsoitteistoDto> filtered = new ArrayList<>();
        for (OsoitteistoDto osoite : osoites) {
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

    protected List<SearchResultRowDto> transformToimikuntaJasens(List<AituToimikuntaResultDto> aituToimikuntas,
                                                                 SearchResultPresentation presentation) {
        List<SearchResultRowDto> results = new ArrayList<>();

        Set<AituToimikuntaJasenAggregateDto> aggregates = new LinkedHashSet<>();
        for (final AituToimikuntaResultDto toimikunta : aituToimikuntas) {
            new Combiner<>(
                src -> new AituToimikuntaJasenAggregateDto(toimikunta,
                    src.get(AituJasenyysDto.class).orNull())).combinedWith(AituJasenyysDto.class, toimikunta.getJasenyydet())
                .atLeastOne().to(aggregates);
        }
        for (AituToimikuntaJasenAggregateDto aggregate : aggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto(), presentation.getLocale());
            results.add(row);
        }

        return results;
    }

    protected List<SearchResultRowDto> transformNayttotutkinonJarjestajaOrganisaatios(
            List<AituOppilaitosResultDto> aituOppilaitos, SearchResultPresentation presentation) {
        List<SearchResultRowDto> results = new ArrayList<>();

        Set<AituOppilaitosVastuuhenkiloAggregateDto> aggregates = new LinkedHashSet<>();
        for (final AituOppilaitosResultDto oppilaitos : aituOppilaitos) {
            Combiner<AituOppilaitosVastuuhenkiloAggregateDto> combiner = new Combiner<>(
                src -> new AituOppilaitosVastuuhenkiloAggregateDto(
                    src.get(AituOppilaitosResultDto.class).get(),
                    src.get(AituTutkintoDto.class).orNull())
            ).withRepeated(AituOppilaitosResultDto.class, Arrays.asList(oppilaitos));
            if (presentation.isNayttotutkinnonJarjestajaVastuuhenkilosIncluded()) {
                Collection<AituTutkintoDto> tutkintos = CollectionHelper.collect(oppilaitos.getSopimukset(),
                        CollectionHelper.filter(AituSopimusDto.TUTKINNOT,
                                AituTutkintoDto.WITH_VASTUUHENKILO));
                if (!presentation.isNayttotutkinnonJarjestajaOrganisaatiosIncluded() && tutkintos.isEmpty()) {
                    continue;
                }
                combiner.with(AituTutkintoDto.class, tutkintos);
            }
            if (presentation.isNayttotutkinnonJarjestajaOrganisaatiosIncluded()) {
                combiner.atLeastOne();
            }
            combiner.to(aggregates);
        }
        for (AituOppilaitosVastuuhenkiloAggregateDto aggregate : aggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto(), presentation.getLocale());
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
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getNimi() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    to.setNimi(localized(from.getNimi(), locale, DEFAULT_LOCALE));
                }
            });
        }

        if (presentation.isKayntiosoiteIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getKayntiosoite() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    to.setKayntiosoite(dtoConverter.convert(from.getKayntiosoite(), new SearchResultOsoiteDto(), locale));
                    if (to.getKayntiosoite() != null && from.getKayntiosoite() != null
                            && from.getKayntiosoite().getPostinumeroUri() != null) {
                        UiKoodiItemDto postinumeroKoodi  =  koodistoService
                                .findPostinumeroByKoodiUri(locale, from.getKayntiosoite().getPostinumeroUri());
                        if (postinumeroKoodi != null) {
                            to.getKayntiosoite().setPostinumero(postinumeroKoodi.getKoodiId());
                            to.getKayntiosoite().setPostitoimipaikka(postinumeroKoodi.getNimi());
                        }
                    }
                }
            });
        }

        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getKotikunta() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    if (from.getKotipaikkaUri() != null) {
                        UiKoodiItemDto kuntaKoodi = koodistoService.findKuntaByKoodiUri(locale, from.getKotipaikkaUri());
                        if (kuntaKoodi != null) {
                            to.setKotikunta(kuntaKoodi.getNimi());
                        }
                    }
                }
            });
        }

        if (presentation.isOrganisaatiotunnisteIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getOppilaitosKoodi() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    to.setOppilaitosKoodi(from.getOppilaitosKoodi());
                }
            });
        }

        if (presentation.isWwwOsoiteIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getWwwOsoite() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioYhteystietoElementtiDto> elementtis =
                            CollectionHelper.filter(from.getYhteystietoArvos(),
                                new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", locale),
                                new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", DEFAULT_LOCALE))
                                .iterator();
                    if (elementtis.hasNext()) {
                        to.setWwwOsoite(elementtis.next().getArvo());
                    }
                    Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                            CollectionHelper.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(DEFAULT_LOCALE))
                                    .iterator();
                    if (yhteystietos.hasNext()) {
                        to.setWwwOsoite(yhteystietos.next().getWww());
                    }
                }
            });
        }

        if (presentation.isOrganisaatioEmailIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getEmailOsoite() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                            CollectionHelper.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(DEFAULT_LOCALE))
                                    .iterator();
                    if (yhteystietos.hasNext()) {
                        to.setEmailOsoite(yhteystietos.next().getEmail());
                    }
                }
            });
        }

        if (presentation.isPuhelinnumeroIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getPuhelinnumero() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                            CollectionHelper.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(DEFAULT_LOCALE))
                                    .iterator();
                    if (yhteystietos.hasNext()) {
                        to.setPuhelinnumero(yhteystietos.next().getNumero());
                    }
                }
            });
        }

        if(presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getKriisitiedotuksenEmail() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                            CollectionHelper.filter(from.getYhteystietoArvos(),
                                    new OrganisaatioYksityiskohtainenYhteystietoArvoByKriisiEmailPredicate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoArvoByKriisiEmailPredicate(DEFAULT_LOCALE))
                                    .iterator();
                    if(yhteystietoArvos.hasNext()) {
                        to.setKriisitiedotuksenEmail(yhteystietoArvos.next().getArvo());
                    }
                }
            });
        }

        if(presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) { return from.getVarhaiskasvatuksenYhteyshenkilo() == null; }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                            CollectionHelper.filter(from.getYhteystietoArvos(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(DEFAULT_LOCALE))
                                    .iterator();
                    if(yhteystietoArvos.hasNext()) {
                        to.setVarhaiskasvatuksenYhteyshenkilo(yhteystietoArvos.next().getArvo());
                    }
                }
            });
        }

        if(presentation.isVarhaiskasvatuksenEmailIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) { return from.getVarhaiskasvatuksenEmail() == null; }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                            CollectionHelper.filter(from.getYhteystietoArvos(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate(locale),
                                    new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate(DEFAULT_LOCALE))
                                    .iterator();
                    if(yhteystietoArvos.hasNext()) {
                        to.setVarhaiskasvatuksenEmail(yhteystietoArvos.next().getArvo());
                    }
                }
            });
        }

        // TODO: viranomaistiedotuksenEmail, koulutusneuvonnanEmail
        copyDetails(results, context, copiers, presentation.getLocale());
    }

    protected interface DetailCopier {

        boolean isMissing(SearchResultRowDto from);

        void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale);

    }

    protected void copyDetails(List<SearchResultRowDto> results, CamelRequestContext context,
                               List<DetailCopier> copiers, Locale locale) {
        Map<String, OrganisaatioDetailsDto> cache = new HashMap<>();

        if (copiers.isEmpty()) {
            return;
        }

        boolean newDetailsFetched = false;
        try {
            Map<String,String> oidsByOppilaitosKoodi = new HashMap<>();
            // Go through each row and use all included copiers to check content. if content is missing add the details to the result.
            results: for (SearchResultRowDto result : results) {
                String oid = result.getOrganisaatioOid();
                if (oid == null && result.getOppilaitosKoodi() != null) {
                    oid = oidsByOppilaitosKoodi.get(result.getOppilaitosKoodi());
                    if (oid == null) {
                        try {
                            oid = organisaatioService.findOidByOppilaitoskoodi(result.getOppilaitosKoodi());
                        } catch(Exception e) {
                            result.setNimi(getMessage("organisaatio_oid_not_found_by_oppilaitoskoodi", locale, result.getOppilaitosKoodi()));
                            continue results;
                        }
                        oidsByOppilaitosKoodi.put(result.getOppilaitosKoodi(), oid);
                    }
                }
                if (oid != null) {
                    for (DetailCopier copier : copiers) {
                        if(copier.isMissing(result)) {
                            OrganisaatioDetailsDto details;
                            if (cache.containsKey(oid)) {
                                details = cache.get(oid);
                            } else {
                                long rc = context.getRequestCount();
                                try {
                                    details = organisaatioService.getdOrganisaatioByOid(oid, context);
                                } catch(Exception e) {
                                    result.setNimi(getMessage("orgnisaatio_not_found_by_oid", locale, oid));
                                    continue results;
                                }
                                if (context.getRequestCount() != rc) {
                                    newDetailsFetched = true;
                                }
                                cache.put(oid, details);
                            }
                            copier.copy(details, result, locale);
                        }
                    }

                }
            }
        } finally {
            if (newDetailsFetched)  {
                organisaatioService.updateOrganisaatioYtunnusDetails(context);
            }
        }
    }

    @Override
    public String getLoggeInUserOid() {
        return super.getLoggedInUserOid();
    }

    @Override
    public void produceExcel(HSSFWorkbook workbook, SearchResultsPresentationDto searchResults) {
        HSSFSheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, searchResults.getPresentation());
        OphCellStyles.OphHssfCellStyles ophHssfCellStyles = new OphCellStyles.OphHssfCellStyles(workbook);
        for (SearchResultRowDto row : searchResults.getRows()) {
            produceRow(searchResults.getPresentation(), sheet, ++rowNum, row, ophHssfCellStyles);
        }
        for (int i = 0; i < maxColumn; ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    protected int produceHeader(HSSFSheet sheet, int rowNum, int cellNum, SearchResultPresentation presentation) {
        if (presentation.isOrganisaationNimiIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_nimi");
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_tunniste");
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo");
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo_email");
        }
        if (presentation.isOrganisaatioEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_email");
        }
        if (presentation.isPositosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite_postinumero");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite_postitoimipaikka");
        }
        if (presentation.isKayntiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_kayntiosoite");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_kayntiosoite_postinumero");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_kayntiosoite_postitoimipaikka");
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_puhelinnumero");
        }
        if (presentation.isWwwOsoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_www_osoite");
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_viranomaistiedotuksen_email");
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_koulutusneuvonnan_email");
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_kriisitiedotuksen_email");
        }
        if (presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_varhaiskasvatuksen_yhteyshenkilo");
        }
        if (presentation.isVarhaiskasvatuksenEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_varhaiskasvatuksen_email");
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_sijaintikunta");
        }
        return cellNum - 1;
    }

    private String osoite(SearchResultOsoiteDto osoite) {
        if (osoite == null) {
            return "";
        }
        return join(" ",
                osoite.getOsoite(),
                osoite.getExtraRivi()//,
                //join(" ", osoite.getPostinumero(), osoite.getPostitoimipaikka())
        );
    }

    private void produceRow(SearchResultPresentation presentation, HSSFSheet sheet, int rowNum, SearchResultRowDto row, OphCellStyles.OphHssfCellStyles ophHssfCellStyles) {
        int cellNum = 0;
        if (presentation.isOrganisaationNimiIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getNimi(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOppilaitosKoodi(), ophHssfCellStyles);
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(" ", row.getYhteystietoNimi()), ophHssfCellStyles);
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getHenkiloEmail(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaatioEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getEmailOsoite(), ophHssfCellStyles);
        }
        if (presentation.isPositosoiteIncluded()) {
            SearchResultOsoiteDto osoite = row.getPostiosoite();
            value(cell(sheet, rowNum, cellNum++), osoite(osoite), ophHssfCellStyles);
            String postinumero = null,
                    postitoimipaikka = null;
            if (osoite != null) {
                postinumero = osoite.getPostinumero();
                postitoimipaikka = osoite.getPostitoimipaikka();
            }
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postinumero).or(""), ophHssfCellStyles);
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postitoimipaikka).or(""), ophHssfCellStyles);
        }
        if (presentation.isKayntiosoiteIncluded()) {
            SearchResultOsoiteDto osoite = row.getKayntiosoite();
            value(cell(sheet, rowNum, cellNum++), osoite(osoite), ophHssfCellStyles);
            String postinumero = null,
                    postitoimipaikka = null;
            if (osoite != null) {
                postinumero = osoite.getPostinumero();
                postitoimipaikka = osoite.getPostitoimipaikka();
            }
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postinumero).or(""), ophHssfCellStyles);
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postitoimipaikka).or(""), ophHssfCellStyles);
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getPuhelinnumero(), ophHssfCellStyles);
        }
        if (presentation.isWwwOsoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getWwwOsoite(), ophHssfCellStyles);
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getViranomaistiedotuksenEmail(), ophHssfCellStyles);
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoulutusneuvonnanEmail(), ophHssfCellStyles);
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKriisitiedotuksenEmail(), ophHssfCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenYhteyshenkilo(), ophHssfCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenEmail(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKotikunta(), ophHssfCellStyles);
        }

    }

    protected HSSFCell value(HSSFCell cell, String value, OphCellStyles.OphHssfCellStyles ophHssfCellStyles) {
        if (value != null) {
            cell.setCellValue(value);
        }
        ophHssfCellStyles.apply(cell);
        return cell;
    }

    protected String getMessage(String key, Locale locale, Object... params) {
        try {
            return messageSource.getMessage(key, params, locale);
        } catch (NoSuchMessageException e) {
            return messageSource.getMessage(key, params, DEFAULT_LOCALE);
        }
    }

    protected Cell header(HSSFCell cell, SearchResultPresentation presentation, String localizationKey) {
        Locale locale = presentation.getLocale();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        String value = getMessage(localizationKey, locale);
        OphCellStyles.OphHssfCellStyles ophCellStyles = new OphCellStyles.OphHssfCellStyles(cell.getSheet().getWorkbook());
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        ophCellStyles.visit(hssfCellStyle -> hssfCellStyle.setFont(font));
        if (value != null) {
            cell.setCellValue(value);
        }
        ophCellStyles.apply(cell);
        return cell;
    }

    protected HSSFCell cell(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        HSSFCell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }

    public void setOrganisaatioService(OrganisaatioService organisaatioService) {
        this.organisaatioService = organisaatioService;
    }
}
