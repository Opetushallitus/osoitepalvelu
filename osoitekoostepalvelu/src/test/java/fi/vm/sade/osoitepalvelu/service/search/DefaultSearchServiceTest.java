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

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituJasenyysDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituToimikuntaResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.DefaultAituService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTermDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.DefaultSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForHenkilosException;
import fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForKoulutusException;
import fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForOrganisaatiosException;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
import fi.vm.sade.osoitepalvelu.service.mock.AituServiceMock;
import fi.vm.sade.osoitepalvelu.service.mock.KoodistoServiceRouteMock;
import fi.vm.sade.osoitepalvelu.service.mock.OrganisaatioServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.slf4j.LoggerFactory;

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 9:22 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestAppConfig.class})
public class DefaultSearchServiceTest {
    private OrganisaatioServiceMock organisaatioRouteMock;
    private KoodistoServiceRouteMock koodistoRouteMock;
    private AituServiceMock aituServiceMock;

    @Autowired
    private DefaultSearchService defaultSearchService;

    @Autowired
    private DefaultKoodistoService defaultKoodistoService;

    @Autowired
    private DefaultAituService defaultAituService;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    @Before
    public void init() {
        this.organisaatioRouteMock  =  new OrganisaatioServiceMock();
        this.koodistoRouteMock  =  new KoodistoServiceRouteMock();
        this.aituServiceMock = new AituServiceMock(defaultAituService);

        this.defaultSearchService.setOrganisaatioService(this.organisaatioRouteMock);

        this.defaultKoodistoService.setKoodistoRoute(this.koodistoRouteMock);
        this.defaultSearchService.setKoodistoService(this.defaultKoodistoService);

        this.defaultSearchService.setAituService(aituServiceMock);
    }

    private AituOppilaitos createAituOppilaitos(int id) {
        AituOppilaitos oppilaitos = new AituOppilaitos();
        Map<String, String> nimimap = new HashMap<String, String>();
        nimimap.put("fi", "testioppilaitos" + id);
        oppilaitos.setNimi(nimimap);
        oppilaitos.setOid("000000000" + id);
        oppilaitos.setOppilaitoskoodi("0000" + id);
        oppilaitos.setOsoite("testikatu " + id);
        oppilaitos.setPostinumero("2500" + id);
        oppilaitos.setPostitoimipaikka("Espoo");
        //oppilaitos.setSopimukset(null);
        return oppilaitos;
    }

    private AituJasenyysDto createAituJasenyys(int id, String rooli) {
        AituJasenyysDto jasenyys = new AituJasenyysDto();
        jasenyys.setAidinkieli("fi");
        jasenyys.setEdustus("asiantuntija");
        jasenyys.setEtunimi("Etu" + id);
        jasenyys.setOsoite("Hakakatu " + id);
        jasenyys.setPostinumero("2500" + id);
        jasenyys.setPostitoimipaikka("Turku");
        jasenyys.setRooli(rooli);
        jasenyys.setSahkoposti("etu" + id + ".suku" + id + "@turku.fi");
        jasenyys.setSukunimi("Suku" + id);
        jasenyys.setVoimassa(true);
        return jasenyys;
    }

    private AituToimikunta createAituToimikunta(int id) {
        AituToimikunta toimikunta = new AituToimikunta();
        toimikunta.setId(""+ id);
        toimikunta.setKielisyys("fi");
        Map<String, String> nimimap = new HashMap<String, String>();
        nimimap.put("fi", "testitoimikunta" + id);
        toimikunta.setNimi(nimimap);
        toimikunta.setSahkoposti("testi.toimi" + id + "@kun.ta");
        toimikunta.setToimikausi(AituToimikunta.AituToimikausi.voimassa);
        return toimikunta;
    }

    @Test
    public void testAituToimikuntaJasenet() throws
                TooFewSearchConditionsForOrganisaatiosException, TooFewSearchConditionsForHenkilosException, TooFewSearchConditionsForKoulutusException {
        // Lisätään oppilaitos-testidata
        List<AituOppilaitos> oppilaitokses = new LinkedList<AituOppilaitos>();
        oppilaitokses.add(createAituOppilaitos(1));
        oppilaitokses.add(createAituOppilaitos(2));
        aituServiceMock.setOppilaitokses(oppilaitokses);

        // Lisätään toimikunta-id -testidata
        List<String> toimikuntaIds = new LinkedList<String>();
        toimikuntaIds.add("1");
        toimikuntaIds.add("2");
        aituServiceMock.setToimikuntaIds(toimikuntaIds);

        // LIsätään toimikunta-testidata
        List<AituToimikunta> toimikuntas = new LinkedList<AituToimikunta>();
        AituToimikunta toimikunta1 = createAituToimikunta(1);
        List<AituJasenyysDto> jasenyydet = new LinkedList<AituJasenyysDto>();
        jasenyydet.add(createAituJasenyys(1, "Puheenjohtaja"));
        jasenyydet.add(createAituJasenyys(2, "Sihteeri"));
        toimikunta1.setJasenyydet(jasenyydet);
        toimikuntas.add(toimikunta1);

        AituToimikunta toimikunta2 = createAituToimikunta(2);
        List<AituJasenyysDto> jasenyydet2 = new LinkedList<AituJasenyysDto>();
        jasenyydet2.add(createAituJasenyys(3, "Puheenjohtaja"));
        toimikunta2.setJasenyydet(jasenyydet2);
        toimikuntas.add(toimikunta2);

        AituToimikunta toimikunta3 = createAituToimikunta(3);
        toimikuntas.add(toimikunta3);
        aituServiceMock.setToimikuntas(toimikuntas);

        // Jasenet-haku
        SearchTermsDto terms = new SearchTermsDto();
        List<SearchTargetGroupDto> targetGroups = new ArrayList<SearchTargetGroupDto>();
        SearchTargetGroupDto targetGroup = new SearchTargetGroupDto();
        targetGroup.setType(SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT);
        targetGroup.setOptions(Arrays.asList(new SearchTargetGroup.TargetType[]
                {SearchTargetGroup.TargetType.JASENET}));
        targetGroups.add(targetGroup);
        terms.setTargetGroups(targetGroups);
        SearchTermDto term = new SearchTermDto();
        term.setType(SearchTermDto.TERM_TUTKINTOIMIKUNTA_ROOLIS);
        term.setValues(Arrays.asList(new String[] {"Sihteeri"}));
        terms.getTerms().add(term);
        SearchResultsDto results = this.defaultSearchService.find(terms, new DefaultCamelRequestContext());
        assertEquals("Toimikuntia", 3, results.getAituToimikuntas().size());
        assertEquals("Jaseniä 1:ssä", 1, results.getAituToimikuntas().get(0).getJasenyydet().size());
        assertEquals("Jaseniä 2:ssa", 0, results.getAituToimikuntas().get(1).getJasenyydet().size());
        assertEquals("Jaseniä 3:ssa", 0, results.getAituToimikuntas().get(2).getJasenyydet().size());
    }

    @Test
    public void testAituToimikuntaSahkoposti() throws
                TooFewSearchConditionsForOrganisaatiosException, TooFewSearchConditionsForHenkilosException, TooFewSearchConditionsForKoulutusException {
        // Lisätään oppilaitos-testidata
        List<AituOppilaitos> oppilaitokses = new LinkedList<AituOppilaitos>();
        oppilaitokses.add(createAituOppilaitos(1));
        oppilaitokses.add(createAituOppilaitos(2));
        aituServiceMock.setOppilaitokses(oppilaitokses);

        // Lisätään toimikunta-id -testidata
        List<String> toimikuntaIds = new LinkedList<String>();
        toimikuntaIds.add("1");
        toimikuntaIds.add("2");
        aituServiceMock.setToimikuntaIds(toimikuntaIds);

        // LIsätään toimikunta-testidata
        List<AituToimikunta> toimikuntas = new LinkedList<AituToimikunta>();
        AituToimikunta toimikunta1 = createAituToimikunta(1);
        List<AituJasenyysDto> jasenyydet = new LinkedList<AituJasenyysDto>();
        jasenyydet.add(createAituJasenyys(1, "Puheenjohtaja"));
        jasenyydet.add(createAituJasenyys(2, "Sihteeri"));
        toimikunta1.setJasenyydet(jasenyydet);
        toimikuntas.add(toimikunta1);

        AituToimikunta toimikunta2 = createAituToimikunta(2);
        List<AituJasenyysDto> jasenyydet2 = new LinkedList<AituJasenyysDto>();
        jasenyydet2.add(createAituJasenyys(3, "Puheenjohtaja"));
        toimikunta2.setJasenyydet(jasenyydet2);
        toimikuntas.add(toimikunta2);

        AituToimikunta toimikunta3 = createAituToimikunta(3);
        toimikuntas.add(toimikunta3);
        aituServiceMock.setToimikuntas(toimikuntas);

        // toimikunnan sähköpostiosoitteen haku
        SearchTermsDto terms2 = new SearchTermsDto();
        List<SearchTargetGroupDto> targetGroups2 = new ArrayList<SearchTargetGroupDto>();
        SearchTargetGroupDto targetGroup2 = new SearchTargetGroupDto();
        targetGroup2.setType(SearchTargetGroup.GroupType.TUTKINTOTOIMIKUNNAT);
        targetGroup2.setOptions(Arrays.asList(new SearchTargetGroup.TargetType[]
                {SearchTargetGroup.TargetType.VIRANOMAIS_EMAIL, SearchTargetGroup.TargetType.JASENET}));
        targetGroups2.add(targetGroup2);
        terms2.setTargetGroups(targetGroups2);
        SearchTermDto term2 = new SearchTermDto();
        term2.setType(SearchTermDto.TERM_TUTKINTOIMIKUNTA_ROOLIS);
        term2.setValues(Arrays.asList(new String[] {"Sihteeri"}));
        terms2.getTerms().add(term2);
        SearchResultsDto results2 = this.defaultSearchService.find(terms2, new DefaultCamelRequestContext());
        assertEquals("Toimikuntia", 3, results2.getAituToimikuntas().size());
        for (AituJasenyysDto j : results2.getAituToimikuntas().get(0).getJasenyydet()) {
            logger.info(".....J: "+ j.getKokoNimi());
        }
        assertEquals("Jaseniä 1:ssä", 1, results2.getAituToimikuntas().get(0).getJasenyydet().size());
        for (AituToimikuntaResultDto a : results2.getAituToimikuntas()) {
            assertNotNull(a.getSahkoposti());
        }
        assertEquals("Jaseniä 2:ssa", 0, results2.getAituToimikuntas().get(1).getJasenyydet().size());
        assertEquals("Jaseniä 3:ssa", 0, results2.getAituToimikuntas().get(2).getJasenyydet().size());

    }

    @Test
    public void testOrganisaatioResultsReturned() throws
                TooFewSearchConditionsForOrganisaatiosException, TooFewSearchConditionsForHenkilosException, TooFewSearchConditionsForKoulutusException {
        List<OrganisaatioYhteystietoHakuResultDto> yhteystietos  =  new ArrayList<OrganisaatioYhteystietoHakuResultDto>();
        OrganisaatioYhteystietoHakuResultDto yhteystieto  =  new OrganisaatioYhteystietoHakuResultDto();
        yhteystieto.setOid("OID");
        yhteystieto.setOppilaitosKoodi("OPKOODI");
        yhteystieto.setKotipaikka("Jurmala");
        yhteystieto.setKielet(Arrays.asList(new String[]{"fi", "sv"}));
        Map<String, String> nimi  =  new HashMap<String, String>();
        nimi.put("fi", "Koulu");
        nimi.put("sv", "Skolan");
        yhteystieto.setNimi(nimi);
        yhteystieto.setTyypit(Arrays.asList(new String[]{"Oppilaitos"}));
        List<OrganisaatioYhteysosoiteDto> osoittees  =  new ArrayList<OrganisaatioYhteysosoiteDto>();
        OrganisaatioYhteysosoiteDto osoite  =  new OrganisaatioYhteysosoiteDto();
        osoite.setKieli("fi");
        osoite.setOsoite("Oppijankuja 6");
        osoite.setPostinumero("12345");
        osoite.setOsoiteTyyppi(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.kaynti);
        osoittees.add(osoite);
        yhteystieto.setKayntiosoite(osoittees);
        yhteystieto.setPostiosoite(new ArrayList<OrganisaatioYhteysosoiteDto>());
        yhteystietos.add(yhteystieto);
        organisaatioRouteMock.setOrganisaatioYhteystietoResults(yhteystietos);

        SearchTermsDto terms  =  new SearchTermsDto();
        List<SearchTargetGroupDto> targetGroups  =  new ArrayList<SearchTargetGroupDto>();
        SearchTargetGroupDto targetGroup  =  new SearchTargetGroupDto();
        targetGroup.setType(SearchTargetGroup.GroupType.OPPILAITOKSET);
        targetGroup.setOptions(Arrays.asList(new SearchTargetGroup.TargetType[]
                {SearchTargetGroup.TargetType.ORGANISAATIO}));
        targetGroups.add(targetGroup);
        terms.setTargetGroups(targetGroups);
        SearchTermDto term = new SearchTermDto();
        term.setType(SearchTermDto.TERM_KUNTAS);
        term.setValues(Arrays.asList(new String[] {"JOKULA"}));
        terms.getTerms().add(term);
        SearchResultsDto results  =  this.defaultSearchService.find(terms, new DefaultCamelRequestContext());
        assertNotNull(results.getOrganisaatios());
        assertEquals(1, results.getOrganisaatios().size());
        OrganisaatioResultDto firstResult  =  results.getOrganisaatios().get(0);
        assertEquals("OID", firstResult.getOid());
        assertEquals("OPKOODI", firstResult.getOppilaitosKoodi());
        assertEquals(nimi, firstResult.getNimi());
        assertEquals(1, firstResult.getKayntiosoite().size());
        assertEquals("Oppijankuja 6", firstResult.getKayntiosoite().get(0).getOsoite());
        assertEquals("kaynti", firstResult.getKayntiosoite().get(0).getOsoiteTyyppi());

        targetGroup.setOptions(Arrays.asList(new SearchTargetGroup.TargetType[0]));
        results = this.defaultSearchService.find(terms, new DefaultCamelRequestContext());
        assertEquals(0, results.getOrganisaatios().size());
    }
}
