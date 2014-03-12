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

package fi.vm.sade.osoitepalvelu.reitit;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoVersioDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route.KoodistoRoute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringTestAppConfig.class, OsoitepalveluCamelConfig.class })
public class KoodistoReittiTest {

    @Autowired
    private KoodistoRoute koodistoReitti;

    @Test
    public void testHaeKaikkiOppilaitosTyypit() {
        KoodistoTyyppi tyyppi = KoodistoTyyppi.OPPILAITOSTYYPPI;
        List<KoodiDto> oppilaitosTyypit = koodistoReitti.haeKooditKoodistonTyyppilla(tyyppi);
        Assert.assertNotNull(oppilaitosTyypit);
        Assert.assertTrue(oppilaitosTyypit.size() > 0);
    }

    @Test
    public void testHaeOppilaitosTyypitKoodistonVersiotJaKooditVersiolla() {
        KoodistoTyyppi tyyppi = KoodistoTyyppi.OPPILAITOSTYYPPI;
        List<KoodistoVersioDto> versiot = koodistoReitti.haeKoodistonVersiot(tyyppi);
        Assert.assertNotNull(versiot);
        Assert.assertTrue(versiot.size() > 0);

        // Testataan, toimiiko tässä haku tietylle koodiston versiolle
        KoodistoVersioDto versio = versiot.get(0);
        List<KoodiDto> oppilaitosTyypit = koodistoReitti.haeKooditKoodistonVersiolleTyyppilla(tyyppi,
                versio.getVersio());
        Assert.assertNotNull(oppilaitosTyypit);
        Assert.assertTrue(oppilaitosTyypit.size() > 0);
    }
}
