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

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.KayttajahakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.ResultAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
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
    private final Locale DEFAULT_LOCALE = new Locale("fi","FI");

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<SearchResultRowDto> aggregateResultRows(List<OrganisaatioResultDto> results,
                                                        SearchResultPresentation presentation) {
        Set<ResultAggregateDto> aggregates = new LinkedHashSet<ResultAggregateDto>();
        for (OrganisaatioResultDto result : results) {
            List<OsoitteistoDto> filteredOsoites = filterOsoites(result.getPostiosoite(), presentation.getLocale());
            for (OsoitteistoDto osoite : filteredOsoites) {
                for (KayttajahakuResultDto kayttaja : result.getYhteyshenkilöt() ) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, osoite));
                }
                if(result.getYhteyshenkilöt().size() < 1) {
                    aggregates.add(new ResultAggregateDto(result, null, osoite));
                }
            }
            if( filteredOsoites.size() < 1 ) {
                for (KayttajahakuResultDto kayttaja : result.getYhteyshenkilöt() ) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, null));
                }
            }
            if( result.getPostiosoite().size() < 1 && result.getYhteyshenkilöt().size() < 1 ) {
                aggregates.add(new ResultAggregateDto(result, null, null));
            }
        }
        List<SearchResultRowDto> aggregatedResults = new ArrayList<SearchResultRowDto>();
        for( ResultAggregateDto aggregate : aggregates ) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            row.setNimi(localized(aggregate.getOrganisaatio().getNimi(), presentation.getLocale(), DEFAULT_LOCALE));
            if( presentation.isResultRowIncluded(row) ) {
                aggregatedResults.add(row);
            }
        }
        return aggregatedResults;
    }

    @Override
    public String getLoggeInUserOid() {
        return super.getLoggedInUserOid();
    }

    protected String localized(Map<String,String> nimi, Locale preferredLocale, Locale defaultLocale) {
        if (nimi == null || nimi.isEmpty()) {
            return null;
        }
        if (preferredLocale != null) {
            if (nimi.containsKey(preferredLocale.toString())) {
                return nimi.get(preferredLocale.toString());
            }
            if (nimi.containsKey(preferredLocale.getLanguage())) {
                return nimi.get(preferredLocale.getLanguage());
            }
        }
        if( !EqualsHelper.equals(preferredLocale, defaultLocale)) {
            return localized(nimi, defaultLocale, defaultLocale);
        }
        return null;
    }

    protected List<OsoitteistoDto> filterOsoites(List<OsoitteistoDto> osoites, Locale locale) {
        if (locale==null || osoites.size() < 1) {
            return osoites;
        }
        List<OsoitteistoDto> filtered = new ArrayList<OsoitteistoDto>();
        for( OsoitteistoDto osoite : osoites ) {
            if (EqualsHelper.equals(locale.getLanguage(), osoite.getKieli())
                    || EqualsHelper.equals(locale.toString(), osoite.getKieli())) {
                filtered.add(osoite);
            }
        }
        if( filtered.size() < 1 ) {
            return filterOsoites(osoites, DEFAULT_LOCALE);
        }
        return filtered;
    }

    @Override
    public void produceExcel(Workbook workbook, List<SearchResultRowDto> rows, SearchResultPresentation presentation) {
        Sheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, presentation);
        for (SearchResultRowDto row : rows) {
            if( presentation.isResultRowIncluded(row) ) {
                produceRow(presentation, sheet, ++rowNum, row);
            }
        }
        for (int i = 0; i < maxColumn; ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    protected int produceHeader(Sheet sheet, int rowNum, int cellNum, SearchResultPresentation presentation) {
        if (presentation.isOrganisaationNimiIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_nimi");
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_tunniste");
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo");
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo_email");
        }
        if (presentation.isPositosoiteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite");
        }
        if (presentation.isKatuosoiteIncluded() && presentation.isPostinumeroIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_katuosoite_and_postinumero");
        }
        if (presentation.isPLIncluded() && presentation.isPostinumeroIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_pl_and_postinumero");
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_puhelinnumero");
        }
        if (presentation.isFaksinumeroIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_faksinumero");
        }
        if (presentation.isWwwOsoiteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_www_osoite");
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_viranomaistiedotuksen_email");
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_koulutusneuvonnan_email");
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_kriisitiedotuksen_email");
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            header( cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_sijaintikunta");
        }
        return cellNum-1;
    }

    private void produceRow(SearchResultPresentation presentation, Sheet sheet, int rowNum, SearchResultRowDto row) {
        int cellNum = 0;
        if (presentation.isOrganisaationNimiIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getNimi() );
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getOrganisaatioOid() );
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            value( cell(sheet, rowNum, cellNum++), join(" ", row.getEtunimi(), row.getSukunimi()) );
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getEmail() );
        }
        if (presentation.isPositosoiteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), join("\n",
                    row.getOsoite(),
                    row.getExtraRivi(),
                    join(" ", row.getPostinumero(), row.getPostitoimipaikka())
            ) );
        }
        if (presentation.isKatuosoiteIncluded() && presentation.isPostinumeroIncluded()) {
            value( cell(sheet, rowNum, cellNum++), join(", ", row.getOsoite(), row.getPostinumero()) );
        }
        if (presentation.isPLIncluded() && presentation.isPostinumeroIncluded()) {
            value( cell(sheet, rowNum, cellNum++), join(", ", row.getPostilokero(), row.getPostinumero()));
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getPuhelinnumero() );
        }
        if (presentation.isFaksinumeroIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getFaksinumero() );
        }
        if (presentation.isWwwOsoiteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getWwwOsoite() );
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getVirnaomaistiedotuksenEmail() );
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getKoulutusneuvonnanEmail() );
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getKriisitiedotuksenEmail() );
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            value( cell(sheet, rowNum, cellNum++), row.getKotikunta() );
        }
    }

    protected String localized(Map<String,String> nimi, Locale preferredLocale) {
        if (nimi == null || nimi.isEmpty()) {
            return null;
        }
        if (preferredLocale != null) {
            if (nimi.containsKey(preferredLocale.toString())) {
                return nimi.get(preferredLocale.toString());
            }
            if (nimi.containsKey(preferredLocale.getLanguage())) {
                return nimi.get(preferredLocale.getLanguage());
            }
        }
        if( !EqualsHelper.equals(preferredLocale, DEFAULT_LOCALE)) {
            return localized(nimi, DEFAULT_LOCALE);
        }
        return null;
    }

    protected Cell value(Cell cell, String value) {
        if( value != null ) {
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
        if( value != null ) {
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
}
