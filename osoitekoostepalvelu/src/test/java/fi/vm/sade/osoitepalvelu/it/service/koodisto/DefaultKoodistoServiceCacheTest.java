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

package fi.vm.sade.osoitepalvelu.it.service.koodisto;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.IntegrationTest;
import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.DefaultKoodistoRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto;
import fi.vm.sade.osoitepalvelu.service.koodisto.DefaultKoodistoServiceTest;

/**
 * User: ratamaa
 * Date: 12/30/13
 * Time: 12:40 PM
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {SpringTestAppConfig.class, OsoitepalveluCamelConfig.class })
public class DefaultKoodistoServiceCacheTest {
    private static final Locale LOCALE_FI  =  new Locale("fi", "FI");
    private static final long DEFAULT_CACHE_TIMEOUT_MS  =  1000L * 3600L;
    
    @Autowired
    private DefaultKoodistoService koodistoService;

    @Autowired
    private DefaultKoodistoRoute koodistoReitti;

    @Test
    public void testCache() {
        try {
            long defaultCache  =  koodistoService.getCacheTimeoutSeconds(), orignalSearchCount  =  koodistoReitti
                    .getFindCounterValue();
            koodistoReitti.setFindCounterUsed(true);

            // Ensure cache turned off:
            koodistoService.setCacheTimeoutSeconds(-1L);

            List<UiKoodiItemDto> optiot  =  koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
            DefaultKoodistoServiceTest.assertListNonEmptyAndItemsOfType(optiot,
                    KoodistoDto.KoodistoTyyppi.ALUEHALLINTOVIRASTO);

            // Ensure cache not used:
            assertEquals(orignalSearchCount  +  1L, koodistoReitti.getFindCounterValue());

            // Turn cache on:
            koodistoService.setCacheTimeoutSeconds(DEFAULT_CACHE_TIMEOUT_MS);

            List<UiKoodiItemDto> optiot2  =  koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
            // Ensure cache used:
            assertEquals(orignalSearchCount  +  2L, koodistoReitti.getFindCounterValue());

            // And that results match the original:
            DefaultKoodistoServiceTest.assertListNonEmptyAndItemsOfType(optiot2,
                    KoodistoDto.KoodistoTyyppi.ALUEHALLINTOVIRASTO);
            assertEquals(optiot.size(), optiot2.size());

            koodistoService.setCacheTimeoutSeconds(defaultCache);
        } catch(CamelExecutionException e) {
            ignoreIf503(e);
        }
    }

    private void ignoreIf503(CamelExecutionException e) throws CamelExecutionException {
        if (e.getCause() != null && e.getCause() instanceof HttpOperationFailedException) {
            if (((HttpOperationFailedException) e.getCause()).getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                return;
            }
        }
        throw e;
    }

}
