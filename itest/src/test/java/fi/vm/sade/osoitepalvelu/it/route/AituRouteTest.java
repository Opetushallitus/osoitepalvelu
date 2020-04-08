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
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.configuration.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.route.AituRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOsoitepalveluResultsDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 4/9/14
 * Time: 5:37 PM
 */
@Ignore // Not available in Bamboo
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  { SpringItestConfig.class, OsoitepalveluCamelConfig.class })
public class AituRouteTest {

    @Autowired
    private AituRoute aituRoute;

    @Test
    public void testResultsFound() {
        AituOsoitepalveluResultsDto result  =  aituRoute.findAituResults(new DefaultCamelRequestContext());
        assertNotNull(result);
        assertTrue(result.getToimikunnat().size() > 0);
        assertTrue(result.getOppilaitokset().size() > 0);
    }

}
