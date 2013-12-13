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
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOSTYYPPI);
	}
	
	@Test
	public void testHaeOmistajaTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeOmistajaTyyppiValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OMISTAJATYYPPI);
	}
	
	@Test
	public void testHaeVuosiluokkaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeVuosiluokkaValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.VUOSILUOKAT);
	}
	
	@Test
	public void testHaeMaakuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeMaakuntaValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.MAAKUNTA);
	}
	
	@Test
	public void testHaeKuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeKuntaValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KUNTA);
	}
	
	@Test
	public void testHaeTutkintoTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeTutkintoTyyppiValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTOTYYPPI);
	}
	
	@Test
	public void testHaeTutkintoValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeTutkintoValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTO);
	}
	
	@Test
	public void testHaeOppilaitoksenOpetuskieliValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeOppilaitoksenOpetuskieliValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI);
	}
	
	@Test
	public void testHaeKoulutuksenKieliValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeKoulutuksenKieliValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA);
	}
	
	@Test
	public void testHaeKoulutusAsteValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeKoulutusAsteValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSASTEKELA);
	}
	
	@Test
	public void testHaeKoulutuksenJarjestejaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeKoulutuksenJarjestejaValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSTOIMIJA);
	}
	
	@Test
	public void testHaeOpintoAlaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeOpintoAlaValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPINTOALAOPH2002);
	}
	
	@Test
	public void testHaeAlueHallintoVirastoValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.haeAlueHallintoVirastoValinnat(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
	}
	
	private <T> void assertListNotEmpty(List<T> arvot, String arvojoukonNimi) {
		Assert.assertNotNull(arvot);
		Assert.assertTrue("Virhe: Lista '" + arvojoukonNimi + "' ei saa olla tyhjä!", 
				arvot.size() > 0);
	}
	
	private void assertListNonEmptyAndItemsOfType(List<UiKoodiItemDto> optiot, KoodistoTyyppi tyyppi) {
		assertListNotEmpty(optiot, tyyppi.getUri());
		for (UiKoodiItemDto optio : optiot) {
			Assert.assertTrue("Virheellinen koodistotyyppi " + optio.getKoodistonTyyppi().name() + ", vaadittu arvo " +
					tyyppi.name(), optio.getKoodistonTyyppi().equals(tyyppi));
		}
	}
}
