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

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.search.DefaultSearchResultTransformerService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.*;
import junit.framework.Assert;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  {SpringTestAppConfig.class})
public class SearchResultTransformerServiceTest {

    @Autowired
    private DefaultSearchResultTransformerService resultTranformerService;

    @Before
    public void init() {
        resultTranformerService.setOrganisaatioService(null);
    }

    protected SearchResultsDto organisaatioResults(List<OrganisaatioResultDto> organisaatioResultDtos) {
        SearchResultsDto results = new SearchResultsDto();
        results.setOrganisaatios(organisaatioResultDtos);
        return results;
    }

    @Test
    public void testAggregateOrganisaatiotWithYhteyshenkilos() {
        OrganisaatioResultDto organisaatio1  =  new OrganisaatioResultDto(),
                organisaatio2  =  new OrganisaatioResultDto();
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
        OrganisaatioYhteystietoDto yhteyshenkilo1  =  new OrganisaatioYhteystietoDto(),
                yhteyshenkilo2  =  new OrganisaatioYhteystietoDto();
        yhteyshenkilo1.setEmail("email");
        yhteyshenkilo1.setNimi("Etu Suku");
        yhteyshenkilo1.setNimike("A");
        yhteyshenkilo2.setNimi("henk2");
        yhteyshenkilo2.setEmail("email2");
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo2);
        organisaatio1.getNimi().put("fi", "saame"); // saame < suomi, päivittety järjestys

        List<OrganisaatioResultDto> list  =  Arrays.asList(organisaatio1, organisaatio2);
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(organisaatioResults(list),
                new AllColumnsSearchResultPresentation(),
                new DefaultCamelRequestContext(), null);
        assertNotNull(results.getPresentation());
        List<SearchResultRowDto> rows  =  results.getRows();
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
        OrganisaatioResultDto organisaatio1  =  new OrganisaatioResultDto(),
                organisaatio2  =  new OrganisaatioResultDto();
        organisaatio1.setOid("org1");
        organisaatio2.setOid("org2");
        OrganisaatioYhteystietoDto yhteyshenkilo1  =  new OrganisaatioYhteystietoDto(),
                yhteyshenkilo2  =  new OrganisaatioYhteystietoDto();
        yhteyshenkilo1.setEmail("henk1");
        yhteyshenkilo2.setEmail("henk2");
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilot().add(yhteyshenkilo2);

        OsoitteistoDto osoite1  =  new OsoitteistoDto(),
                osoite2  =  new OsoitteistoDto(),
                osoite3  =  new OsoitteistoDto(),
                kayntiosoite1 = new OsoitteistoDto(),
                kayntiosoite2 = new OsoitteistoDto(),
                kayntiosoite3 = new OsoitteistoDto();
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
        kayntiosoite1.setYhteystietoOid("kaynti1");
        kayntiosoite1.setKieli("sv");
        kayntiosoite2.setYhteystietoOid("kaynti2");
        kayntiosoite2.setKieli("sv");
        kayntiosoite3.setYhteystietoOid("kaynti3_ei_palauteta");
        kayntiosoite3.setKieli("fi");
        organisaatio1.getPostiosoite().add(osoite1);
        organisaatio1.getPostiosoite().add(osoite2);
        organisaatio1.getPostiosoite().add(osoite3);
        organisaatio2.getPostiosoite().add(osoite1);
        organisaatio2.getPostiosoite().add(osoite2);
        organisaatio2.getPostiosoite().add(osoite3);
        organisaatio1.getKayntiosoite().add(kayntiosoite1);
        organisaatio1.getKayntiosoite().add(kayntiosoite2);
        organisaatio1.getKayntiosoite().add(kayntiosoite3);

        List<OrganisaatioResultDto> list  =  Arrays.asList(organisaatio1, organisaatio2);
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(organisaatioResults(list),
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")), new DefaultCamelRequestContext(), null);
        List<SearchResultRowDto> rows  =  results.getRows();
        assertEquals(6, rows.size());
        assertEquals("org1", rows.get(0).getOrganisaatioOid());
        assertEquals("org1", rows.get(3).getOrganisaatioOid());
        assertNotNull(rows.get(0).getPostiosoite());
        assertEquals("yht1", rows.get(0).getPostiosoite().getYhteystietoOid());
        assertEquals("extra", rows.get(0).getPostiosoite().getExtraRivi());
        assertEquals("sv", rows.get(0).getPostiosoite().getKieli());
        assertEquals("osoitetyyppi", rows.get(0).getPostiosoite().getTyyppi());
        assertEquals("Rantatie 1", rows.get(0).getPostiosoite().getOsoite());
        assertEquals("12345", rows.get(0).getPostiosoite().getPostinumero());
        assertEquals("TOIJALA", rows.get(0).getPostiosoite().getPostitoimipaikka());
        assertEquals("yht1", rows.get(1).getPostiosoite().getYhteystietoOid());
        assertEquals("yht2", rows.get(2).getPostiosoite().getYhteystietoOid());
        assertEquals("yht2", rows.get(3).getPostiosoite().getYhteystietoOid());
        assertEquals("henk1", rows.get(0).getHenkiloEmail());
        assertEquals("henk2", rows.get(1).getHenkiloEmail());
        assertEquals("henk1", rows.get(2).getHenkiloEmail());
        assertEquals("henk2", rows.get(3).getHenkiloEmail());
        assertEquals("org2", rows.get(4).getOrganisaatioOid());
        assertNotNull(rows.get(4).getPostiosoite());
        assertEquals("yht1", rows.get(4).getPostiosoite().getYhteystietoOid());
        assertEquals("org2", rows.get(5).getOrganisaatioOid());
        assertNotNull(rows.get(5).getPostiosoite());
        assertEquals("yht2", rows.get(5).getPostiosoite().getYhteystietoOid());
        assertNull(rows.get(5).getHenkiloEmail());

        assertEquals("kaynti1", rows.get(0).getKayntiosoite().getYhteystietoOid());
        assertEquals("kaynti2", rows.get(1).getKayntiosoite().getYhteystietoOid());
        assertEquals("kaynti1", rows.get(2).getKayntiosoite().getYhteystietoOid());
        assertEquals("kaynti2", rows.get(3).getKayntiosoite().getYhteystietoOid());
        assertNull(rows.get(4).getKayntiosoite());
        assertNull(rows.get(5).getKayntiosoite());
    }

    @Test
    public void fallbackToFinnishLocaleInPostiosoite() {
        OrganisaatioResultDto organisaatio  =  new OrganisaatioResultDto();
        OsoitteistoDto osoite  =  new OsoitteistoDto(),
                osoite2  =  new OsoitteistoDto();
        osoite.setYhteystietoOid("osoite-fi");
        osoite.setKieli("fi");
        organisaatio.getPostiosoite().add(osoite);

        osoite2.setYhteystietoOid("osoite-en");
        osoite2.setKieli("en");
        organisaatio.getPostiosoite().add(osoite2);

        List<OrganisaatioResultDto> list  =  Arrays.asList(organisaatio);
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(organisaatioResults(list),
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")),
                new DefaultCamelRequestContext(), null);
        List<SearchResultRowDto> rows  =  results.getRows();
        assertEquals(1, rows.size());
        assertNotNull(rows.get(0).getPostiosoite());
        assertEquals("fi", rows.get(0).getPostiosoite().getKieli());
    }

    @Test
    public void testSearchResultPresentationByAddressFieldsDtoFields() {
        SearchTermsDto terms  =  new SearchTermsDto();
        terms.setSearchType(SearchTermsDto.SearchType.CONTACT);
        terms.setAddressFields(SearchResultPresentationByAddressFieldsDto.fieldMappingKeys());
        terms.setLocale(new Locale("fi"));
        new SearchResultPresentationByAddressFieldsDto(terms);
    }

    @Test
    public void testProduceEmptyExcel() {
        HSSFWorkbook wb  =  new HSSFWorkbook();
        resultTranformerService.produceExcel(wb, new SearchResultsPresentationDto(new ArrayList<SearchResultRowDto>(),
                new AllColumnsSearchResultPresentation()));
        assertEquals(1, wb.getNumberOfSheets());
        assertEquals(0, wb.getSheetAt(0).getLastRowNum());
        assertEquals("Organisaatio", wb.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void testProduceSingleLineNullExcel() {
        OrganisaatioResultDto organisaatio  =  new OrganisaatioResultDto();
        HashMap<String, String> nimi  =  new HashMap<String, String>();
        nimi.put("sv", "Organisations namnet");
        nimi.put("fi", "Organisaation nimi");
        organisaatio.setNimi(nimi);
        SearchResultPresentation presentation  =  new AllColumnsSearchResultPresentation();
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(
                organisaatioResults(Arrays.asList(organisaatio)),
                presentation, new DefaultCamelRequestContext(), null);

        HSSFWorkbook wb  =  new HSSFWorkbook();
        resultTranformerService.produceExcel(wb, results);
        assertEquals(1, wb.getNumberOfSheets());
        assertEquals(1, wb.getSheetAt(0).getLastRowNum());
        assertEquals("Organisaation nimi", wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
    }

    protected SearchResultsDto henkiloResults(List<HenkiloHakuResultDto> henkiloHakuResultDtos) {
        SearchResultsDto results = new SearchResultsDto();
        results.setHenkilos(henkiloHakuResultDtos);
        return results;
    }

    @Test
    public void testEmptyResults() {
        List<HenkiloHakuResultDto> list = new ArrayList<HenkiloHakuResultDto>();
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(henkiloResults(list),
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")),
                new DefaultCamelRequestContext(),  null);
        Assert.assertNotNull(results.getRows());
        Assert.assertEquals(0, results.getRows().size());
    }

    @Test
    public void testHenkiloResults() {
        List<HenkiloHakuResultDto> list = list(
            henkilo("henkilo1Oid", "Milla Makkonen",
                    new ArrayList<HenkiloOsoiteDto>()),
            // -> 1 result row
            henkilo("henkilo2Oid", "Matti MAkkonen",
                list(
                    henkiloOsoite("Mikonkuja 6", "matti.mikkonen@example.com"),
                    henkiloOsoite("Muoniontie 7", "matti.mikkonen@muonionkylakoulu.com")
                 )
            ),
            // -> 2 result rows with organisaatioHenkilös and osoittees together
            henkilo("henkilo3Oid", "Heikki Heikkinen",
                    list(
                        henkiloOsoite("Heikintie 2", "heikki.heikkinen@example.com"),
                        henkiloOsoite("Muoniontie 7", "heikki.heikkinen@muonionkylakoulu.com")
                    )
            ),
            // -> 2 result rows for each osoite with empty organisaatioHenkilö details
            henkilo("henkilo4Oid", "Matti Mikkonen",
                    new ArrayList<HenkiloOsoiteDto>()
            )
            // -> 2 result rows for each organisaatiohenkilö with empty osoittees
        );
        SearchResultsPresentationDto results  =  resultTranformerService.transformToResultRows(henkiloResults(list),
                new AllColumnsSearchResultPresentation(new Locale("sv", "SE")).withoutYhteyshenkiloEmail(),
                new DefaultCamelRequestContext(), null);
        assertEquals(6, results.getRows().size());

        // First those withtout organization in alpabetic order:
        assertEquals("henkilo3Oid", results.getRows().get(0).getHenkiloOid());
        assertEquals("heikki.heikkinen@example.com", results.getRows().get(0).getHenkiloEmail());
        assertEquals("henkilo3Oid", results.getRows().get(1).getHenkiloOid());
        assertEquals("heikki.heikkinen@muonionkylakoulu.com", results.getRows().get(1).getHenkiloEmail());

        // Then by organisaatio OID (since organisaatio does not have name to compare against):
        assertEquals("henkilo2Oid", results.getRows().get(2).getHenkiloOid());
        assertNotNull(results.getRows().get(2).getPostiosoite());
        assertEquals("Mikonkuja 6", results.getRows().get(2).getPostiosoite().getOsoite());
        assertEquals("posti", results.getRows().get(2).getPostiosoite().getTyyppi());
        assertEquals("fi", results.getRows().get(2).getPostiosoite().getKieli());
        assertEquals("30100", results.getRows().get(2).getPostiosoite().getPostinumero());
        assertEquals("TAMPERE", results.getRows().get(2).getPostiosoite().getPostitoimipaikka());
        assertEquals("matti.mikkonen@example.com", results.getRows().get(2).getHenkiloEmail());

        assertEquals("henkilo2Oid", results.getRows().get(3).getHenkiloOid());
        assertEquals("matti.mikkonen@muonionkylakoulu.com", results.getRows().get(3).getHenkiloEmail());
        assertEquals("Muoniontie 7", results.getRows().get(3).getPostiosoite().getOsoite());

        assertEquals("henkilo4Oid", results.getRows().get(4).getHenkiloOid());
        assertNull(results.getRows().get(4).getEmailOsoite());
        assertEquals("henkilo1Oid", results.getRows().get(5).getHenkiloOid());
        assertNull(results.getRows().get(5).getEmailOsoite());
    }


    protected<T> List<T> list(T...v) {
        return new ArrayList<T>(Arrays.asList(v));
    }

    private Long id=0L;

    protected HenkiloHakuResultDto henkilo(String oid, String nimi,
                               List<HenkiloOsoiteDto> osoittees) {
        HenkiloHakuResultDto dto = new HenkiloHakuResultDto();
        dto.setHenkiloOid(oid);
        dto.setNimi(nimi);
        dto.setOsoittees(osoittees);
        return dto;
    }

    protected HenkiloOsoiteDto henkiloOsoite(String osoite, String email) {
        HenkiloOsoiteDto dto = new HenkiloOsoiteDto();
        dto.setId(++id);
        dto.setKieli("fi");
        dto.setOsoiteTyyppi("posti");
        dto.setOsoite(osoite);
        dto.setPostinumero("30100");
        dto.setPostitoimipaikka("TAMPERE");
        dto.setHenkiloEmail(email);
        return dto;
    }

}
