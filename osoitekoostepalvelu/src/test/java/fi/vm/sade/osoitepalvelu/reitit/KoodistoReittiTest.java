package fi.vm.sade.osoitepalvelu.reitit;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.kooste.SpringApp;
import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApp.class)
public class KoodistoReittiTest {

	@Autowired
	private KoodistoReitti koodistoReitti;
	
	@Test
	public void testHaeKaikkiOppilaitosTyypit() {
		KoodistoTyyppi tyyppi = KoodistoTyyppi.OPPILAITOSTYYPPI;
		List<KoodiDto> oppilaitosTyypit = koodistoReitti.haeKooditKoodistonTyyppilla(tyyppi);
		Assert.assertNotNull(oppilaitosTyypit);
		Assert.assertTrue(oppilaitosTyypit.size() > 0);
	}
}
