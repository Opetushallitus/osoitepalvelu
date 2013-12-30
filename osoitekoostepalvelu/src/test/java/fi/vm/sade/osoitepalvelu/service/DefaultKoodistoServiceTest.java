package fi.vm.sade.osoitepalvelu.service;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.route.KoodistoReitti;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringTestAppConfig.class})
public class DefaultKoodistoServiceTest {

	private static final Locale LOCALE_FI = new Locale("fi", "FI");
	
	@Autowired
	private DefaultKoodistoService koodistoService;

    @Autowired
    private KoodistoReitti koodistoReitti;
	
	@Test
	public void testHaeOppilaitosTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findOppilaitosTyyppiOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOSTYYPPI);
	}
	
	@Test
	public void testHaeOmistajaTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findOmistajaTyyppiOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OMISTAJATYYPPI);
	}
	
	@Test
	public void testHaeVuosiluokkaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findVuosiluokkaOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.VUOSILUOKAT);
	}
	
	@Test
	public void testHaeMaakuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findMaakuntaOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.MAAKUNTA);
	}
	
	@Test
	public void testHaeKuntaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findKuntaOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KUNTA);
	}
	
	@Test
	public void testHaeTutkintoTyyppiValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findTutkintoTyyppiOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTOTYYPPI);
	}
	
	@Test
	public void testHaeTutkintoValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findTutkintoOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTO);
	}
	
	@Test
	public void testHaeOppilaitoksenOpetuskieliValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findOppilaitoksenOpetuskieliOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI);
	}
	
	@Test
	public void testHaeKoulutuksenKieliValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findKoulutuksenKieliOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA);
	}
	
	@Test
	public void testHaeKoulutusAsteValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findKoulutusAsteOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSASTEKELA);
	}
	
	@Test
	public void testHaeKoulutuksenJarjestejaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findKoulutuksenJarjestejaOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSTOIMIJA);
	}
	
	@Test
	public void testHaeOpintoAlaValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findOpintoAlaOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPINTOALAOPH2002);
	}
	
	@Test
	public void testHaeAlueHallintoVirastoValinnat() {
		List<UiKoodiItemDto> optiot = koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
		assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
	}

    @Test
    public void testCache() {
        long defaultCache = koodistoService.getCacheTimeoutMillis(),
            orignalSearchCount = koodistoReitti.getFindCounterValue();
        koodistoReitti.setFindCounterUsed(true);

        // Ensure cache turned off:
        koodistoService.setCacheTimeoutMillis(-1l);

        List<UiKoodiItemDto> optiot = koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.ALUEHALLINTOVIRASTO);

        // Ensure cache not used:
        assertEquals( orignalSearchCount+1l, koodistoReitti.getFindCounterValue() );

        // Turn cache on:
        koodistoService.setCacheTimeoutMillis(1000l*3600l);

        List<UiKoodiItemDto> optiot2 = koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
        // Ensure cache not used:
        assertEquals(orignalSearchCount + 1l, koodistoReitti.getFindCounterValue());

        // And that results match the original:
        assertListNonEmptyAndItemsOfType(optiot2, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
        assertEquals( optiot.size(), optiot2.size() );

        koodistoService.setCacheTimeoutMillis(defaultCache);
    }
	
	private <T> void assertListNotEmpty(List<T> arvot, String arvojoukonNimi) {
		assertNotNull(arvot);
		assertTrue("Virhe: Lista '" + arvojoukonNimi + "' ei saa olla tyhjä!",
                arvot.size() > 0);
	}
	
	private void assertListNonEmptyAndItemsOfType(List<UiKoodiItemDto> optiot, KoodistoTyyppi tyyppi) {
		assertListNotEmpty(optiot, tyyppi.getUri());
		for (UiKoodiItemDto optio : optiot) {
			assertTrue("Virheellinen koodistotyyppi " + optio.getKoodistonTyyppi().name() + ", vaadittu arvo " +
                    tyyppi.name(), optio.getKoodistonTyyppi().equals(tyyppi));
		}
	}
}
