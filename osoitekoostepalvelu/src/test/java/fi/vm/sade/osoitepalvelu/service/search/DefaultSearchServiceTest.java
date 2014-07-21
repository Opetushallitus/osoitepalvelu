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
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTermDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.DefaultSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForHenkilosException;
import fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForOrganisaatiosException;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
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

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 9:22 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestAppConfig.class })
public class DefaultSearchServiceTest {
    private OrganisaatioServiceMock organisaatioRouteMock;
    private KoodistoServiceRouteMock koodistoRouteMock;

    @Autowired
    private DefaultSearchService defaultSearchService;

    @Autowired
    private DefaultKoodistoService defaultKoodistoService;

    @Before
    public void init() {
        this.organisaatioRouteMock  =  new OrganisaatioServiceMock();
        this.koodistoRouteMock  =  new KoodistoServiceRouteMock();
        this.defaultSearchService.setOrganisaatioService(this.organisaatioRouteMock);

        this.defaultKoodistoService.setKoodistoRoute(this.koodistoRouteMock);
        this.defaultSearchService.setKoodistoService(this.defaultKoodistoService);
    }

    @Test
    public void testOrganisaatioResultsReturned() throws
                TooFewSearchConditionsForOrganisaatiosException, TooFewSearchConditionsForHenkilosException {
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
