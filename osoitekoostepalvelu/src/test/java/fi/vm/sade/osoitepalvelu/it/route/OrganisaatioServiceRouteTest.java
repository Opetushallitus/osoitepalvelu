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

package fi.vm.sade.osoitepalvelu.it.route;

import fi.vm.sade.osoitepalvelu.IntegrationTest;
import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.actors.threadpool.Arrays;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 10:12 AM
 */
@Ignore // No specified username/password pairs in Bamboo for system user
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  { SpringTestAppConfig.class, OsoitepalveluCamelConfig.class })
public class OrganisaatioServiceRouteTest {
    
    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired
    private KoodistoService koodistoService;

    @Test
    public void testFindOrganisaatiosByKuntas() {
        OrganisaatioYhteystietoCriteriaDto criteria  =  new OrganisaatioYhteystietoCriteriaDto();
        criteria.setKuntaList(Arrays.asList(new String[]{"kunta_604", "kunta_400"}));
        List<OrganisaatioYhteystietoHakuResultDto> yhteystietos  =  organisaatioServiceRoute
                .findOrganisaatioYhteystietos(criteria, new DefaultCamelRequestContext());
        assertTrue(yhteystietos.size() > 0);
    }

    @Test
    public void testFindOrganisaatioByOid() {
        String testOid  =  "1.2.246.562.10.00000000001";
        OrganisaatioDetailsDto tiedot
                 =  organisaatioServiceRoute.getdOrganisaatioByOid(testOid, new DefaultCamelRequestContext());
        assertNotNull(tiedot);
        assertEquals(testOid, tiedot.getOid());
    }

    @Test
    public void testFindAllOrganisaatioOids() {
        List<String> oids = organisaatioServiceRoute.findAllOrganisaatioOids(new DefaultCamelRequestContext());
        assertTrue(oids.size() > 0);
    }

    @Test
    public void testFindAllActiveKoulutustoimijas() {
        OrganisaatioHierarchyResultsDto results =
                organisaatioServiceRoute.findOrganisaatioHierachyByTyyppi("Koulutustoimija",
                        new DefaultCamelRequestContext());
        assertNotNull(results);
        assertNotNull(results.getOrganisaatiot());
        assertTrue(results.getOrganisaatiot().size() > 0);
    }
}
