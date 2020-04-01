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
import fi.vm.sade.osoitepalvelu.SpringItestConfig;
import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.route.DefaultAuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloListResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KayttooikesuryhmaDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 3:30 PM
 */
@Ignore // No specified username/password pairs in Bamboo for system user
@Category(value = { IntegrationTest.class } )
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  { SpringItestConfig.class, OsoitepalveluCamelConfig.class })
public class AuthenticationServiceRouteTest {
    @Autowired
    private DefaultAuthenticationServiceRoute authenticationServiceRoute;

    @Test
    public void testFindKayttooikeusryhmas() {
        List<KayttooikesuryhmaDto> ryhmas  =  authenticationServiceRoute.findKayttooikeusryhmas(
                new DefaultCamelRequestContext());
        assertTrue(ryhmas.size() > 0);
    }

    @Test
    public void testFindHenkilos() {
        List<String> oids = Arrays.asList(new String[]{
                "1.2.246.562.10.93326837098",
                "1.2.246.562.10.59574166134"
        });
        HenkiloCriteriaDto criteria = new HenkiloCriteriaDto();
        criteria.setOrganisaatioOids(oids);
        List<HenkiloListResultDto> henkilos  =  authenticationServiceRoute.findHenkilos(criteria,
                new DefaultCamelRequestContext());
        assertTrue(henkilos.size() > 0);
    }

    @Test
    public void testFindHenikiloByOid() {
        String oid = "1.2.246.562.24.00000000001";
        DefaultCamelRequestContext requestContext = new DefaultCamelRequestContext();
        HenkiloDetailsDto details = authenticationServiceRoute.getHenkiloTiedot(oid, requestContext);
        assertNotNull(details);

        details = authenticationServiceRoute.getHenkiloTiedot(oid, requestContext);
        assertNotNull(details);
    }
}
