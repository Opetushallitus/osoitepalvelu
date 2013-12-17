package fi.vm.sade.osoitepalvelu.reitit;

import java.util.List;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.route.KoodistoReitti;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.kooste.SpringApp;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoVersioDto;

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
	
	@Test
	public void testHaeOppilaitosTyypitKoodistonVersiotJaKooditVersiolla() {
		KoodistoTyyppi tyyppi = KoodistoTyyppi.OPPILAITOSTYYPPI;
		List<KoodistoVersioDto> versiot = koodistoReitti.haeKoodistonVersiot(tyyppi);
		Assert.assertNotNull(versiot);
		Assert.assertTrue(versiot.size() > 0);
		
		// Testataan, toimiiko tässä haku tietylle koodiston versiolle
		KoodistoVersioDto versio = versiot.get(0);
		List<KoodiDto> oppilaitosTyypit = koodistoReitti.haeKooditKoodistonVersiolleTyyppilla(tyyppi, versio.getVersio());
		Assert.assertNotNull(oppilaitosTyypit);
		Assert.assertTrue(oppilaitosTyypit.size() > 0);
	}
}
