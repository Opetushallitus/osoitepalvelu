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

package fi.vm.sade.osoitepalvelu.service.search;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 4:05 PM
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultTransformerService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultPresentationByAddressFieldsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringTestAppConfig.class})
public class SearchResultTransformerServiceTest {

    @Autowired
    private SearchResultTransformerService resultTranformerService;

    @Test
    public void testAggregateOrganisaatiotWithYhteyshenkilos() {
        OrganisaatioResultDto organisaatio1 = new OrganisaatioResultDto(),
                organisaatio2 = new OrganisaatioResultDto();
        organisaatio1.setOid("org1");
        organisaatio2.setOid("org2");
        organisaatio2.setEmailOsoite("org email");
        organisaatio2.setFaksinumero("fax");
        organisaatio2.setPuhelinnumero("org puh");
        organisaatio2.setKotikunta("kotikunta");
        organisaatio2.setNimi(new HashMap<String, String>());
        organisaatio2.getNimi().put("fi", "suomi");
        organisaatio2.getNimi().put("sv", "svenska");
        organisaatio2.setToimipistekoodi("toimipiste");
        organisaatio2.setOppilaitosKoodi("oppilaitoskoodi");
        organisaatio2.setTyypit(new ArrayList<String>(Arrays.asList("Tyyppi1", "Tyyppi2")));
        organisaatio2.setWwwOsoite("www");
        OrganisaatioYhteystietoDto yhteyshenkilo1 = new OrganisaatioYhteystietoDto(),
                yhteyshenkilo2 = new OrganisaatioYhteystietoDto();
        yhteyshenkilo1.setEmail("email");
        yhteyshenkilo1.setNimi("Etu Suku");
        yhteyshenkilo1.setNimike("A");
        yhteyshenkilo2.setNimi("henk2");
        yhteyshenkilo2.setEmail("email2");
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo2);

        List<OrganisaatioResultDto> list = Arrays.asList(organisaatio1, organisaatio2);
        SearchResultsDto results = resultTranformerService.transformToResultRows(list,
                new AllColumnsSearchResultPresentation());
        assertNotNull(results.getPresentation());
        List<SearchResultRowDto> rows = results.getRows();
        assertEquals(3, rows.size());
        assertEquals("org1", rows.get(0).getOrganisaatioOid());
        assertEquals("email", rows.get(0).getHenkiloEmail());
        assertEquals("Etu Suku", rows.get(0).getYhteystietoNimi());
        assertEquals("org1", rows.get(1).getOrganisaatioOid());
        assertEquals("henk2", rows.get(1).getYhteystietoNimi());
        assertEquals("org2", rows.get(2).getOrganisaatioOid());
        assertEquals("org email", rows.get(2).getEmailOsoite());
        assertEquals("org puh", rows.get(2).getPuhelinnumero());
        assertEquals("kotikunta", rows.get(2).getKotikunta());
        assertEquals("toimipiste", rows.get(2).getToimipistekoodi());
        assertEquals("oppilaitoskoodi", rows.get(2).getOppilaitosKoodi());
        assertEquals(2, rows.get(2).getTyypit().size());
        assertEquals("suomi", rows.get(2).getNimi());
        assertNull(rows.get(2).getHenkiloEmail());
    }


    @Test
    public void testAggregateOrganisaatiotWithYhteyshenkiloAndPostiosoites() {
        OrganisaatioResultDto organisaatio1 = new OrganisaatioResultDto(),
                organisaatio2 = new OrganisaatioResultDto();
        organisaatio1.setOid("org1");
        organisaatio2.setOid("org2");
        OrganisaatioYhteystietoDto yhteyshenkilo1 = new OrganisaatioYhteystietoDto(),
                yhteyshenkilo2 = new OrganisaatioYhteystietoDto();
        yhteyshenkilo1.setEmail("henk1");
        yhteyshenkilo2.setEmail("henk2");
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo2);

        OsoitteistoDto osoite1 = new OsoitteistoDto(),
                osoite2 = new OsoitteistoDto(),
                osoite3 = new OsoitteistoDto();
        osoite1.setYhteystietoOid("yht1");
        osoite1.setExtraRivi("extra");
        osoite1.setKieli("sv");
        osoite1.setOsoiteTyyppi("osoitetyyppi");
        osoite1.setOsoite("Rantatie 1");
        osoite1.setPostinumero("12345");
        osoite1.setPostitoimipaikka("TOIJALA");
        osoite2.setYhteystietoOid("yht2");
        osoite2.setKieli("sv");
        osoite3.setYhteystietoOid("yht3");
        osoite3.setKieli("fi");
        organisaatio1.getPostiosoite().add(osoite1);
        organisaatio1.getPostiosoite().add(osoite2);
        organisaatio1.getPostiosoite().add(osoite3);
        organisaatio2.getPostiosoite().add(osoite1);
        organisaatio2.getPostiosoite().add(osoite2);
        organisaatio2.getPostiosoite().add(osoite3);

        List<OrganisaatioResultDto> list = Arrays.asList(organisaatio1, organisaatio2);
        SearchResultsDto results = resultTranformerService.transformToResultRows(list,
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")));
        List<SearchResultRowDto> rows = results.getRows();
        assertEquals(6, rows.size());
        assertEquals("org1", rows.get(0).getOrganisaatioOid());
        assertEquals("org1", rows.get(3).getOrganisaatioOid());
        assertEquals("yht1", rows.get(0).getYhteystietoOid());
        assertEquals("extra", rows.get(0).getExtraRivi());
        assertEquals("sv", rows.get(0).getOsoiteKieli());
        assertEquals("osoitetyyppi", rows.get(0).getOsoiteTyyppi());
        assertEquals("Rantatie 1", rows.get(0).getOsoite());
        assertEquals("12345", rows.get(0).getPostinumero());
        assertEquals("TOIJALA", rows.get(0).getPostitoimipaikka());
        assertEquals("yht1", rows.get(1).getYhteystietoOid());
        assertEquals("yht2", rows.get(2).getYhteystietoOid());
        assertEquals("yht2", rows.get(3).getYhteystietoOid());
        assertEquals("henk1", rows.get(0).getHenkiloEmail());
        assertEquals("henk2", rows.get(1).getHenkiloEmail());
        assertEquals("henk1", rows.get(2).getHenkiloEmail());
        assertEquals("henk2", rows.get(3).getHenkiloEmail());
        assertEquals("org2", rows.get(4).getOrganisaatioOid());
        assertEquals("yht1", rows.get(4).getYhteystietoOid());
        assertEquals("org2", rows.get(5).getOrganisaatioOid());
        assertEquals("yht2", rows.get(5).getYhteystietoOid());
        assertNull(rows.get(5).getHenkiloEmail());
    }

    @Test
    public void fallbackToFinnishLocaleInPostiosoite() {
        OrganisaatioResultDto organisaatio = new OrganisaatioResultDto();
        OsoitteistoDto osoite = new OsoitteistoDto(),
                osoite2 = new OsoitteistoDto();
        osoite.setYhteystietoOid("osoite-fi");
        osoite.setKieli("fi");
        organisaatio.getPostiosoite().add(osoite);

        osoite2.setYhteystietoOid("osoite-en");
        osoite2.setKieli("en");
        organisaatio.getPostiosoite().add(osoite2);

        List<OrganisaatioResultDto> list = Arrays.asList(organisaatio);
        SearchResultsDto results = resultTranformerService.transformToResultRows(list,
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")));
        List<SearchResultRowDto> rows = results.getRows();
        assertEquals(1, rows.size());
        assertEquals("fi", rows.get(0).getOsoiteKieli());
    }

    @Test
    public void testSearchResultPresentationByAddressFieldsDtoFields() {
        SearchTermsDto terms = new SearchTermsDto();
        terms.setSearchType(SearchTermsDto.SearchType.CONTACT);
        terms.setAddressFields(SearchResultPresentationByAddressFieldsDto.fieldMappingKeys());
        new SearchResultPresentationByAddressFieldsDto(terms, new Locale("fi"));
    }

    @Test
    public void testProduceEmptyExcel() {
        Workbook wb = new HSSFWorkbook();
        resultTranformerService.produceExcel(wb, new SearchResultsDto(new ArrayList<SearchResultRowDto>(),
                new AllColumnsSearchResultPresentation()));
        assertEquals(1, wb.getNumberOfSheets());
        assertEquals(0, wb.getSheetAt(0).getLastRowNum());
        assertEquals("Organisaatio", wb.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void testProduceSingleLineNullExcel() {
        OrganisaatioResultDto organisaatio = new OrganisaatioResultDto();
        HashMap<String,String> nimi = new HashMap<String, String>();
        nimi.put("sv", "Organisations namnet");
        nimi.put("fi", "Organisaation nimi");
        organisaatio.setNimi(nimi);
        SearchResultPresentation presentation = new AllColumnsSearchResultPresentation();
        SearchResultsDto results = resultTranformerService.transformToResultRows(Arrays.asList(organisaatio), presentation);

        Workbook wb = new HSSFWorkbook();
        resultTranformerService.produceExcel(wb, results);
        assertEquals(1, wb.getNumberOfSheets());
        assertEquals(1, wb.getSheetAt(0).getLastRowNum());
        assertEquals("Organisaation nimi", wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
    }
}
