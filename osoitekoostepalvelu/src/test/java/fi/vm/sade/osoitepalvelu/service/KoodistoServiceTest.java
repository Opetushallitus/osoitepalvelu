package fi.vm.sade.osoitepalvelu.service;

import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.kooste.SpringApp;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApp.class)
public class KoodistoServiceTest {

	private static final Locale LOCALE_FI = new Locale("fi", "FI");
	
	@Autowired
	private KoodistoService koodistoService;
	
	@Test
	public void testHaeOppilaitosTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeOppilaitosTyyppiValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "oppilaitostyypit");
		assertListItemsOfType(optiot, KoodistoTyyppi.OPPILAITOSTYYPPI);
	}
	
	@Test
	public void testHaeOmistajaTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeOmistajaTyyppiValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "omistajatyypit");
		assertListItemsOfType(optiot, KoodistoTyyppi.OMISTAJATYYPPI);
	}
	
	@Test
	public void testHaeVuosiluokkaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeVuosiluokkaValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "vuosiluokat");
		assertListItemsOfType(optiot, KoodistoTyyppi.VUOSILUOKAT);
	}
	
	@Test
	public void testHaeMaakuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeMaakuntaValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "maakunnat");
		assertListItemsOfType(optiot, KoodistoTyyppi.MAAKUNTA);
	}
	
	@Test
	public void testHaeKuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeKuntaValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "kunnat");
		assertListItemsOfType(optiot, KoodistoTyyppi.KUNTA);
	}
	
	@Test
	public void testHaeTutkintoTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeTutkintoTyyppiValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "tutkintotyypit");
		assertListItemsOfType(optiot, KoodistoTyyppi.TUTKINTOTYYPPI);
	}
	
	@Test
	public void testHaeTutkintoValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeTutkintoValinnat(LOCALE_FI);
		assertListNotEmpty(optiot, "tutkinnot");
		assertListItemsOfType(optiot, KoodistoTyyppi.TUTKINTO);
	}
	
	private <T> void assertListNotEmpty(List<T> arvot, String arvojoukonNimi) {
		Assert.assertNotNull(arvot);
		Assert.assertTrue("Virhe: Lista '" + arvojoukonNimi + "' ei saa olla tyhjä!", 
				arvot.size() > 0);
	}
	
	private void assertListItemsOfType(List<UiKoodiItemDto> optiot, KoodistoTyyppi tyyppi) {
		for (UiKoodiItemDto optio : optiot) {
			Assert.assertTrue("Virheellinen koodistotyyppi " + optio.getKoodistonTyyppi().name() + ", vaadittu arvo " +
					tyyppi.name(), optio.getKoodistonTyyppi().equals(tyyppi));
		}
	}
}
