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

package fi.vm.sade.osoitepalvelu.it.reitit;

import fi.vm.sade.osoitepalvelu.IntegrationTest;
import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KayttooikesuryhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route.DefaultAuthenticationServiceRoute;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 3:30 PM
 */
@Ignore // missing system user authentication properties
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringTestAppConfig.class, OsoitepalveluCamelConfig.class })
public class AuthenticationServiceRouteTest {
    @Autowired
    private DefaultAuthenticationServiceRoute authenticationServiceRoute;

    @Test
    public void testFindKayttooikeusryhmas() {
        List<KayttooikesuryhmaDto> ryhmas = authenticationServiceRoute.findKayttooikeusryhmas();
        assertTrue( ryhmas.size() > 0 );
    }
}
