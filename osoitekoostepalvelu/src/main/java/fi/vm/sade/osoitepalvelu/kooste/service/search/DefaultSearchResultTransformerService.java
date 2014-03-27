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
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper.join;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:32 PM
 */
@Service
public class DefaultSearchResultTransformerService extends AbstractService
        implements SearchResultTransformerService {
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
                                                SearchResultPresentation presentation, CamelRequestContext context) {
        List<SearchResultRowDto> transformedResults = new ArrayList<SearchResultRowDto>();

        List<SearchResultRowDto> organisaatioResults = transformOrganisaatios(results.getOrganisaatios(), presentation);
        transformedResults.addAll(organisaatioResults);

        List<SearchResultRowDto> henkiloResults = transformHenkilos(results.getHenkilos(), presentation);
        transformedResults.addAll(henkiloResults);

        resolveMissingOrganisaatioRelatedDetails(transformedResults, presentation, context);

        List<SearchResultRowDto> rows = transformedResults;
        if (!organisaatioResults.isEmpty() && !henkiloResults.isEmpty()) {
            // Mix the organisation and henkilo based rows by organisaatio's oid
            rows = applyOrdering(rows);
        }

        return new SearchResultsPresentationDto(rows, presentation);
    }

    protected List<SearchResultRowDto> applyOrdering(List<SearchResultRowDto> transformedResults) {
        return Ordering
                .natural().nullsLast()
                .onResultOf(new Function<SearchResultRowDto, String>() {
                    @Override
                    public String apply(SearchResultRowDto row) {
                        return row.getOrganisaatioOid();
                    }
                })
        .compound(Ordering
                .natural().nullsFirst()
                .onResultOf(new Function<SearchResultRowDto, String>() {
                    @Override
                    public String apply(SearchResultRowDto row) {
                        return row.getHenkiloOid();
                    }
        })).sortedCopy(transformedResults);
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
        List<SearchResultRowDto> transformedResults = new ArrayList<SearchResultRowDto>();

        Set<OrganisaatioResultAggregateDto> organisaatioAggregates = new LinkedHashSet<OrganisaatioResultAggregateDto>();
        for (OrganisaatioResultDto result : results) {
            List<OsoitteistoDto> filteredOsoites = filterOsoites(result.getPostiosoite(), presentation.getLocale());
            for (OsoitteistoDto osoite : filteredOsoites) {
                for (OrganisaatioYhteystietoDto kayttaja : result.getYhteyshenkilot()) {
                    organisaatioAggregates.add(new OrganisaatioResultAggregateDto(result, kayttaja, osoite));
                }
                if (result.getYhteyshenkilot().size() < 1) {
                    organisaatioAggregates.add(new OrganisaatioResultAggregateDto(result, null, osoite));
                }
            }
            if (filteredOsoites.size() < 1) {
                for (OrganisaatioYhteystietoDto kayttaja : result.getYhteyshenkilot()) {
                    organisaatioAggregates.add(new OrganisaatioResultAggregateDto(result, kayttaja, null));
                }
                if (result.getYhteyshenkilot().size() < 1) {
                    organisaatioAggregates.add(new OrganisaatioResultAggregateDto(result, null, null));
                }
            }
        }
        for (OrganisaatioResultAggregateDto aggregate : organisaatioAggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            row.setNimi(localized(aggregate.getOrganisaatio().getNimi(), presentation.getLocale(), DEFAULT_LOCALE));
            if (presentation.isResultRowIncluded(row)) {
                transformedResults.add(row);
            }
        }
        return transformedResults;
    }

    protected List<OsoitteistoDto> filterOsoites(List<OsoitteistoDto> osoites, Locale locale) {
        if (locale == null || osoites.size() < 1) {
            return osoites;
        }
        List<OsoitteistoDto> filtered = new ArrayList<OsoitteistoDto>();
        for (OsoitteistoDto osoite : osoites) {
            if (LocaleHelper.languageEquals(locale, LocaleHelper.parseLocale(osoite.getKieli(), DEFAULT_LOCALE))) {
                filtered.add(osoite);
            }
        }
        if (filtered.size() < 1 && !LocaleHelper.languageEquals(DEFAULT_LOCALE, locale)) {
            return filterOsoites(osoites, DEFAULT_LOCALE);
        }
        return filtered;
    }

    protected List<SearchResultRowDto> transformHenkilos(List<HenkiloHakuResultDto> henkilos,
                                             SearchResultPresentation presentation) {
        List<SearchResultRowDto> results = new ArrayList<SearchResultRowDto>();

        Set<HenkiloResultAggregateDto> henkiloAggregates = new LinkedHashSet<HenkiloResultAggregateDto>();
        for (HenkiloHakuResultDto result : henkilos) {
            List<HenkiloOsoiteDto> filteredOsoites = result.getOsoittees();
            boolean activeOrganisaatioHenkiloPresent = false;
            for (HenkiloOsoiteDto osoite : filteredOsoites) {
                for (OrganisaatioHenkiloDto organisaatioHenkiloDto : result.getOrganisaatioHenkilos()) {
                    if (!organisaatioHenkiloDto.isPassivoitu()) {
                        henkiloAggregates.add(new HenkiloResultAggregateDto(result, organisaatioHenkiloDto, osoite));
                        activeOrganisaatioHenkiloPresent = true;
                    }
                }
                if (!activeOrganisaatioHenkiloPresent) {
                    henkiloAggregates.add(new HenkiloResultAggregateDto(result, null, osoite));
                }
            }
            if (filteredOsoites.size() < 1) {
                for (OrganisaatioHenkiloDto organisaatioHenkiloDto : result.getOrganisaatioHenkilos()) {
                    if (!organisaatioHenkiloDto.isPassivoitu()) {
                        henkiloAggregates.add(new HenkiloResultAggregateDto(result, organisaatioHenkiloDto, null));
                        activeOrganisaatioHenkiloPresent = true;
                    }
                }
                if (!activeOrganisaatioHenkiloPresent) {
                    henkiloAggregates.add(new HenkiloResultAggregateDto(result, null, null));
                }
            }
        }
        for (HenkiloResultAggregateDto aggregate : henkiloAggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            if (presentation.isResultRowIncluded(row)) {
                results.add(row);
            }
        }

        return results;
    }


    protected void resolveMissingOrganisaatioRelatedDetails(List<SearchResultRowDto> results,
                            SearchResultPresentation presentation, CamelRequestContext context) {
        if (organisaatioService == null) {
            return;
        }

        Map<String, OrganisaatioDetailsDto> organisaatioByOidCache = new HashMap<String, OrganisaatioDetailsDto>();
        List<DetailCopier> copiers = new ArrayList<DetailCopier>();

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

        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getKotikunta() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    if (from.getKotipaikkaUri() != null) {
                        UiKoodiItemDto kuntaKoodi  =  koodistoService.findKuntaByKoodiUri(locale, from.getKotipaikkaUri());
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
                            Collections2.filter(from.getYhteystietoArvos(),
                                new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", locale))
                                .iterator();
                    if (elementtis.hasNext()) {
                        to.setWwwOsoite(elementtis.next().getArvo());
                    }
                    Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                            Collections2.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(locale)).iterator();
                    if (yhteystietos.hasNext()) {
                        to.setWwwOsoite(yhteystietos.next().getWww());
                    }
                }
            });
        }

        if (presentation.isYhteyshenkiloEmailIncluded()) {
            copiers.add(new DetailCopier() {
                @Override
                public boolean isMissing(SearchResultRowDto from) {
                    return from.getEmailOsoite() == null;
                }

                @Override
                public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
                    Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                            Collections2.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(locale)).iterator();
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
                            Collections2.filter(from.getYhteystiedot(),
                                    new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(locale)).iterator();
                    if (yhteystietos.hasNext()) {
                        to.setPuhelinnumero(yhteystietos.next().getNumero());
                    }
                }
            });

        }

        // TODO: viranomaistiedotuksenEmail, koulutusneuvonnanEmail, kriisitiedotuksenEmail

        copyDetails(results, context, copiers, presentation.getLocale());
    }

    protected interface DetailCopier {

        boolean isMissing(SearchResultRowDto from);

        void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale);

    }

    protected void copyDetails(List<SearchResultRowDto> results, CamelRequestContext context,
                               List<DetailCopier> copiers, Locale locale) {
        Map<String, OrganisaatioDetailsDto> cache = new HashMap<String, OrganisaatioDetailsDto>();

        if (copiers.isEmpty()) {
            return;
        }

        for (SearchResultRowDto result : results) {
            String oid = result.getOrganisaatioOid();
            if (oid != null) {
                for (DetailCopier copier : copiers) {
                    OrganisaatioDetailsDto details;
                    if (cache.containsKey(oid)) {
                        details = cache.get(oid);
                    } else {
                        details = organisaatioService.getdOrganisaatioByOid(oid, context);
                        cache.put(oid, details);
                    }
                    copier.copy(details, result, locale);
                }

            }
        }
    }

    @Override
    public String getLoggeInUserOid() {
        return super.getLoggedInUserOid();
    }

    @Override
    public void produceExcel(Workbook workbook, SearchResultsPresentationDto searchResults) {
        Sheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, searchResults.getPresentation());
        for (SearchResultRowDto row : searchResults.getRows()) {
            if (searchResults.getPresentation().isResultRowIncluded(row)) {
                produceRow(searchResults.getPresentation(), sheet, ++rowNum, row);
            }
        }
        for (int i = 0; i < maxColumn; ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    protected int produceHeader(Sheet sheet, int rowNum, int cellNum, SearchResultPresentation presentation) {
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
        if (presentation.isPositosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite");
        }
        if (presentation.isKatuosoiteIncluded() && presentation.isPostinumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_katuosoite_and_postinumero");
        }
        if (presentation.isPLIncluded() && presentation.isPostinumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_pl_and_postinumero");
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_puhelinnumero");
        }
        if (presentation.isFaksinumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_faksinumero");
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
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_sijaintikunta");
        }
        return cellNum - 1;
    }

    private void produceRow(SearchResultPresentation presentation, Sheet sheet, int rowNum, SearchResultRowDto row) {
        int cellNum = 0;
        if (presentation.isOrganisaationNimiIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getNimi());
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOppilaitosKoodi());
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(" ", row.getYhteystietoNimi()));
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getHenkiloEmail());
        }
        if (presentation.isPositosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(" ",
                    row.getOsoite(),
                    row.getExtraRivi(),
                    join(" ", row.getPostinumero(), row.getPostitoimipaikka())
            ));
        }
        if (presentation.isKatuosoiteIncluded() && presentation.isPostinumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(", ", row.getOsoite(), row.getPostinumero()));
        }
        if (presentation.isPLIncluded() && presentation.isPostinumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(", ", row.getPostilokero(), row.getPostinumero()));
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getPuhelinnumero());
        }
        if (presentation.isFaksinumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getFaksinumero());
        }
        if (presentation.isWwwOsoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getWwwOsoite());
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getViranomaistiedotuksenEmail());
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoulutusneuvonnanEmail());
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKriisitiedotuksenEmail());
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKotikunta());
        }
    }

    protected Cell value(Cell cell, String value) {
        if (value != null) {
            cell.setCellValue(value);
        }
        return cell;
    }

    protected Cell header(Cell cell, SearchResultPresentation presentation, String localizationKey) {
        Locale locale = presentation.getLocale();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        String value = messageSource.getMessage(localizationKey, new Object[0], locale);
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        cell.setCellStyle(style);
        if (value != null) {
            cell.setCellValue(value);
        }
        return cell;
    }

    protected Cell cell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }

    public void setOrganisaatioService(OrganisaatioService organisaatioService) {
        this.organisaatioService = organisaatioService;
    }
}
