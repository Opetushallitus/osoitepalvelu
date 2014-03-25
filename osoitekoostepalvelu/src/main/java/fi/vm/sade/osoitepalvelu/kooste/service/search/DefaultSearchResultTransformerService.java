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

import com.google.common.collect.Collections2;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtainenYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtaisetTiedotDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioTiedotDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.ResultAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
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
    private static final Locale DEFAULT_LOCALE = new Locale("fi","FI");

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Autowired
    private MessageSource messageSource;

    @Autowired(required = false)
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Override
    public SearchResultsDto transformToResultRows(List<OrganisaatioTiedotDto> results,
                                                          SearchResultPresentation presentation,
                                                          CamelRequestContext context) {
        resolveRelatedVisibleDetails(results, presentation, context);

        Set<ResultAggregateDto> aggregates = new LinkedHashSet<ResultAggregateDto>();
        for (OrganisaatioTiedotDto result : results) {
            List<OsoitteistoDto> filteredOsoites = filterOsoites(result.getPostiosoite(), presentation.getLocale());
            for (OsoitteistoDto osoite : filteredOsoites) {
                for (OrganisaatioYhteystietoDto kayttaja : result.getYhteyshenkilot()) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, osoite));
                }
                if(result.getYhteyshenkilot().size() < 1) {
                    aggregates.add(new ResultAggregateDto(result, null, osoite));
                }
            }
            if(filteredOsoites.size() < 1) {
                for (OrganisaatioYhteystietoDto kayttaja : result.getYhteyshenkilot()) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, null));
                }
            }
            if(result.getPostiosoite().size() < 1 && result.getYhteyshenkilot().size() < 1) {
                aggregates.add(new ResultAggregateDto(result, null, null));
            }
        }
        List<SearchResultRowDto> transformedResults = new ArrayList<SearchResultRowDto>();
        for(ResultAggregateDto aggregate : aggregates) {
            SearchResultRowDto row = dtoConverter.convert(aggregate, new SearchResultRowDto());
            row.setNimi(localized(aggregate.getOrganisaatio().getNimi(), presentation.getLocale(), DEFAULT_LOCALE));
            if(presentation.isResultRowIncluded(row)) {
                transformedResults.add(row);
            }
        }
        return new SearchResultsDto(transformedResults, presentation);
    }

    protected void resolveRelatedVisibleDetails(List<OrganisaatioTiedotDto> results,
                                                SearchResultPresentation presentation,
                                                CamelRequestContext context) {
        if (organisaatioServiceRoute == null) {
            return;
        }

        Map<String,OrganisaatioYksityiskohtaisetTiedotDto> organisaatioByOidCache
                = new HashMap<String, OrganisaatioYksityiskohtaisetTiedotDto>();
        Locale locale = presentation.getLocale();

        if (presentation.isWwwOsoiteIncluded()) {
            copyDetails(results, new DetailCopier(locale, organisaatioByOidCache) {
                @Override
                protected boolean isMissing(OrganisaatioTiedotDto from) {
                    return from.getWwwOsoite() == null;
                }
                @Override
                public void copy(OrganisaatioYksityiskohtaisetTiedotDto from, OrganisaatioTiedotDto to) {
                    Iterator<OrganisaatioYhteystietoElementtiDto> elementtis = Collections2.filter(from.getYhteystietoArvos(),
                            new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", locale)).iterator();
                    if (elementtis.hasNext()) {
                        to.setWwwOsoite(elementtis.next().getArvo());
                    }
                    Iterator<OrganisaatioYksityiskohtainenYhteystietoDto> yhteystietos = Collections2.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(locale)).iterator();
                    if (yhteystietos.hasNext()) {
                        to.setWwwOsoite(yhteystietos.next().getWww());
                    }
                }
            }, context);
        }

        if (presentation.isYhteyshenkiloEmailIncluded()) {
            copyDetails(results, new DetailCopier(locale, organisaatioByOidCache) {
                @Override
                protected boolean isMissing(OrganisaatioTiedotDto from) {
                    return from.getEmailOsoite() == null;
                }
                @Override
                public void copy(OrganisaatioYksityiskohtaisetTiedotDto from, OrganisaatioTiedotDto to) {
                    Iterator<OrganisaatioYksityiskohtainenYhteystietoDto> yhteystietos = Collections2.filter(from.getYhteystiedot(),
                            new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(locale)).iterator();
                    if (yhteystietos.hasNext()) {
                        to.setEmailOsoite(yhteystietos.next().getEmail());
                    }
                }
            }, context);
        }

        if (presentation.isPuhelinnumeroIncluded()) {
            copyDetails(results, new DetailCopier(locale, organisaatioByOidCache) {
                @Override
                protected boolean isMissing(OrganisaatioTiedotDto from) {
                    return from.getPuhelinnumero() == null;
                }
                @Override
                public void copy(OrganisaatioYksityiskohtaisetTiedotDto from, OrganisaatioTiedotDto to) {
                    Iterator<OrganisaatioYksityiskohtainenYhteystietoDto> yhteystietos = Collections2.filter(from.getYhteystiedot(),
                            new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(locale)).iterator();
                    if (yhteystietos.hasNext()) {
                        to.setPuhelinnumero(yhteystietos.next().getNumero());
                    }
                }
            }, context);
        }

        // TODO: ...
    }

    protected abstract class DetailCopier {
        protected Locale locale;
        private Map<String,OrganisaatioYksityiskohtaisetTiedotDto> cache;

        protected DetailCopier(Locale locale, Map<String, OrganisaatioYksityiskohtaisetTiedotDto> cache) {
            this.locale = locale;
            this.cache = cache;
        }

        protected abstract boolean isMissing(OrganisaatioTiedotDto from);

        protected abstract void copy(OrganisaatioYksityiskohtaisetTiedotDto from, OrganisaatioTiedotDto to);
    }

    protected void copyDetails(List<OrganisaatioTiedotDto> results, DetailCopier copier, CamelRequestContext context) {
        for (OrganisaatioTiedotDto result : results) {
            String oid = result.getOid();
            if (oid != null && copier.isMissing(result)) {
                OrganisaatioYksityiskohtaisetTiedotDto details;
                if (copier.cache.containsKey(oid)) {
                    details = copier.cache.get(oid);
                } else {
                    details = organisaatioServiceRoute.getdOrganisaatioByOid(oid, context);
                    copier.cache.put(oid, details);
                }
                copier.copy(details, result);
            }
        }
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
        if(!EqualsHelper.equals(preferredLocale, defaultLocale)) {
            return localized(nimi, defaultLocale, defaultLocale);
        }
        return null;
    }

    protected List<OsoitteistoDto> filterOsoites(List<OsoitteistoDto> osoites, Locale locale) {
        if (locale==null || osoites.size() < 1) {
            return osoites;
        }
        List<OsoitteistoDto> filtered = new ArrayList<OsoitteistoDto>();
        for (OsoitteistoDto osoite : osoites) {
            if (LocaleHelper.languageEquals(locale, LocaleHelper.parseLocale(osoite.getKieli(),DEFAULT_LOCALE))) {
                filtered.add(osoite);
            }
        }
        if (filtered.size() < 1 && !LocaleHelper.languageEquals(DEFAULT_LOCALE, locale)) {
            return filterOsoites(osoites, DEFAULT_LOCALE);
        }
        return filtered;
    }

    @Override
    public void produceExcel(Workbook workbook, SearchResultsDto searchResults) {
        Sheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, searchResults.getPresentation());
        for (SearchResultRowDto row : searchResults.getRows()) {
            if(searchResults.getPresentation().isResultRowIncluded(row)) {
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
            value(cell(sheet, rowNum, cellNum++), join("\n",
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
        if(!EqualsHelper.equals(preferredLocale, DEFAULT_LOCALE)) {
            return localized(nimi, DEFAULT_LOCALE);
        }
        return null;
    }

    protected Cell value(Cell cell, String value) {
        if(value != null) {
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
        if(value != null) {
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

    public void setOrganisaatioServiceRoute(OrganisaatioServiceRoute organisaatioServiceRoute) {
        this.organisaatioServiceRoute = organisaatioServiceRoute;
    }
}
