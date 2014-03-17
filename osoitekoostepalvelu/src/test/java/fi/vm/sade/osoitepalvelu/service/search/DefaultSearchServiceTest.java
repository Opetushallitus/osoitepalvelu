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
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.DefaultSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
import fi.vm.sade.osoitepalvelu.service.mock.KoodistoServiceRouteMock;
import fi.vm.sade.osoitepalvelu.service.mock.OrganisaatioServiceRouteMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 9:22 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringTestAppConfig.class})
public class DefaultSearchServiceTest {
    private OrganisaatioServiceRouteMock organisaatioRouteMock;
    private KoodistoServiceRouteMock koodistoRouteMock;

    @Autowired
    private DefaultSearchService defaultSearchService;

    @Autowired
    private DefaultKoodistoService defaultKoodistoService;

    @Before
    public void init() {
        this.organisaatioRouteMock = new OrganisaatioServiceRouteMock();
        this.koodistoRouteMock = new KoodistoServiceRouteMock();
        this.defaultSearchService.setOrganisaatioServiceRoute(this.organisaatioRouteMock);

        this.defaultKoodistoService.setKoodistoRoute(this.koodistoRouteMock);
        this.defaultSearchService.setKoodistoService(this.defaultKoodistoService);
    }

    @Test
    public void testResultsReturned() {
        List<OrganisaatioYhteystietoHakuResultDto> yhteystietos = new ArrayList<OrganisaatioYhteystietoHakuResultDto>();
        OrganisaatioYhteystietoHakuResultDto yhteystieto = new OrganisaatioYhteystietoHakuResultDto();
        yhteystieto.setOid("OID");
        yhteystieto.setOppilaitosKoodi("OPKOODI");
        yhteystieto.setKotipaikka("Jurmala");
        yhteystieto.setKielet(Arrays.asList(new String[]{"fi", "sv"}));
        Map<String,String> nimi = new HashMap<String, String>();
        nimi.put("fi", "Koulu");
        nimi.put("sv", "Skolan");
        yhteystieto.setNimi(nimi);
        yhteystieto.setTyypit(Arrays.asList(new String[]{"Peruskoulu"}));
        List<OrganisaatioYhteysosoiteDto> osoittees = new ArrayList<OrganisaatioYhteysosoiteDto>();
        OrganisaatioYhteysosoiteDto osoite = new OrganisaatioYhteysosoiteDto();
        osoite.setKieli("fi");
        osoite.setOsoite("Oppijankuja 6");
        osoite.setPostinumero("12345");
        osoite.setOsoiteTyyppi(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.kaynti);
        osoittees.add(osoite);
        yhteystieto.setKayntiosoite(osoittees);
        yhteystieto.setPostiosoite(new ArrayList<OrganisaatioYhteysosoiteDto>());
        yhteystietos.add(yhteystieto);
        organisaatioRouteMock.setOrganisaatioYhteystietoResults(yhteystietos);

        SearchTermsDto terms = new SearchTermsDto();
        OrganisaatioResultsDto results = this.defaultSearchService.find(terms);
        assertNotNull(results.getResults());
        assertEquals(1, results.getResults().size());
        OrganisaatioResultDto firstResult = results.getResults().get(0);
        assertEquals("OID", firstResult.getOid());
        assertEquals("OPKOODI", firstResult.getOppilaitosKoodi());
        assertEquals(nimi, firstResult.getNimi());
        assertEquals(1, firstResult.getKayntiosoite().size());
        assertEquals("Oppijankuja 6", firstResult.getKayntiosoite().get(0).getOsoite());
        assertEquals("kaynti", firstResult.getKayntiosoite().get(0).getOsoiteTyyppi());
    }
}
