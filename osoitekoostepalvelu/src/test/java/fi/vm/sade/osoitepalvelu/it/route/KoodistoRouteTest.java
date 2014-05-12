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
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.route.DefaultKoodistoRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoVersioDto;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  { SpringTestAppConfig.class, OsoitepalveluCamelConfig.class })
public class KoodistoRouteTest {

    @Autowired
    private DefaultKoodistoRoute koodistoReitti;

    @Test
    public void testHaeKaikkiOppilaitosTyypit() {
        try {
            KoodistoTyyppi tyyppi  =  KoodistoTyyppi.OPPILAITOSTYYPPI;
            List<KoodiDto> oppilaitosTyypit  =  koodistoReitti.findKooditKoodistonTyyppilla(tyyppi);
            Assert.assertNotNull(oppilaitosTyypit);
            Assert.assertTrue(oppilaitosTyypit.size() > 0);
        } catch(CamelExecutionException e) {
            ignoreIf503(e);
        }
    }

    private void ignoreIf503(CamelExecutionException e) throws CamelExecutionException {
        if (e.getCause() != null && e.getCause() instanceof HttpOperationFailedException) {
            if (((HttpOperationFailedException) e.getCause()).getStatusCode() == 503) {
                return;
            }
        }
        throw e;
    }

    @Test
    public void testHaeOppilaitosTyypitKoodistonVersiotJaKooditVersiolla() {
        try {
            KoodistoTyyppi tyyppi  =  KoodistoTyyppi.OPPILAITOSTYYPPI;
            List<KoodistoVersioDto> versiot  =  koodistoReitti.findKoodistonVersiot(tyyppi);
            Assert.assertNotNull(versiot);
            Assert.assertTrue(versiot.size() > 0);

            // Testataan, toimiiko tässä haku tietylle koodiston versiolle
            KoodistoVersioDto versio  =  versiot.get(0);
            List<KoodiDto> oppilaitosTyypit  =  koodistoReitti.findKooditKoodistonVersiolleTyyppilla(tyyppi,
                    versio.getVersio());
            Assert.assertNotNull(oppilaitosTyypit);
            Assert.assertTrue(oppilaitosTyypit.size() > 0);
        } catch(CamelExecutionException e) {
            ignoreIf503(e);
        }
    }

    @Test
    public void testFindKuntasByMaakunta() {
        try {
            List<KoodiDto> koodis  =  koodistoReitti.findKoodisWithParent("maakunta_12");
            Assert.assertNotNull(koodis);
            Assert.assertTrue(koodis.size() > 0);
        } catch(CamelExecutionException e) {
            ignoreIf503(e);
        }
    }
}
