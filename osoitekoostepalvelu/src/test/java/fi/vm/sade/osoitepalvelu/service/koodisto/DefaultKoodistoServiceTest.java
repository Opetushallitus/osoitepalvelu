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

package fi.vm.sade.osoitepalvelu.service.koodisto;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.service.koodisto.mock.KoodistoRouteMock;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringTestAppConfig.class, OsoitepalveluCamelConfig.class})
public class DefaultKoodistoServiceTest {
    private static final Locale LOCALE_FI = new Locale("fi", "FI");
    private static final String FI = "fi";
    private static final String SV = "sv";
    private static final String EN = "en";

    @Autowired
    private DefaultKoodistoService koodistoService;

    @Before
    public void init() {
        KoodistoRouteMock mock = new KoodistoRouteMock();
        mock.add( versio(KoodistoTyyppi.OPPILAITOSTYYPPI, 0l), koodi("Yliopisto") );
        mock.add( versio(KoodistoTyyppi.OPPILAITOSTYYPPI, 1l),
                koodi("Yliopisto")
                        .add(arvo(FI, "Yliopisto"))
                        .add(arvo(EN, "Univerisity")),
                koodi("Ammattikoulu") );
        mock.add( versio(KoodistoTyyppi.OMISTAJATYYPPI, 1l), koodi("Testi") );
        mock.add( versio(KoodistoTyyppi.VUOSILUOKAT, 1l), koodi("Testi") );
        mock.add( versio(KoodistoTyyppi.MAAKUNTA, 1l),
                koodi("Ahvenanmaa"),
                koodi("Uusimaa") );
        mock.add( versio(KoodistoTyyppi.KUNTA, 1l), koodi("Helsinki"), koodi("Tampere") );
        mock.add( versio(KoodistoTyyppi.TUTKINTOTYYPPI, 1l), koodi("Toisen asteen ammatillinen koulutus") );
        mock.add( versio(KoodistoTyyppi.TUTKINTO, 1l), koodi("Maisterintutkinto") );
        mock.add( versio(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI, 1l), koodi("suomi") );
        mock.add( versio(KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA, 1l), koodi("suomi"), koodi("englanti") );
        mock.add( versio(KoodistoTyyppi.KOULUTUSASTEKELA, 1l), koodi("Testi") );
        mock.add( versio(KoodistoTyyppi.KOULUTUSTOIMIJA, 1l), koodi("Testi") );
        mock.add( versio(KoodistoTyyppi.OPINTOALAOPH2002, 1l), koodi("Testi") );
        mock.add( versio(KoodistoTyyppi.ALUEHALLINTOVIRASTO, 1l), koodi("Uudenmaan aluehallintovirasto") );

        koodistoService.setKoodistoRoute(mock);
    }

    protected KoodistoVersioDto versio(KoodistoTyyppi tyyppi, long version) {
        return new KoodistoVersioDto( tyyppi.getUri(), tyyppi, version, new LocalDate(), new LocalDate(),
                KoodistoTila.LUONNOS );
    }

    protected KoodiDto koodi(String arvo) {
        return new KoodiDto(arvo);
    }

    protected KoodiArvoDto arvo(String locale, String arvo) {
        return new KoodiArvoDto(arvo, arvo, arvo, locale);
    }

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

    public static <T> void assertListNotEmpty(List<T> arvot, String arvojoukonNimi) {
        assertNotNull(arvot);
        assertTrue("Virhe: Lista '" + arvojoukonNimi + "' ei saa olla tyhjä!", arvot.size() > 0);
    }

    public static void assertListNonEmptyAndItemsOfType(List<UiKoodiItemDto> optiot, KoodistoTyyppi tyyppi) {
        assertListNotEmpty(optiot, tyyppi.getUri());
        for (UiKoodiItemDto optio : optiot) {
            assertTrue("Virheellinen koodistotyyppi " + optio.getKoodistonTyyppi().name() + ", vaadittu arvo " +
                    tyyppi.name(), optio.getKoodistonTyyppi().equals(tyyppi));
        }
    }
}
