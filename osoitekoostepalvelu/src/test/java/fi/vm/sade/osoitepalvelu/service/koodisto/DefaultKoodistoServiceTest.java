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
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.service.mock.AuhenticationServiceRouteMock;
import fi.vm.sade.osoitepalvelu.service.mock.KoodistoServiceRouteMock;
import fi.vm.sade.osoitepalvelu.service.mock.OrganisaatioServiceRouteMock;
import fi.vm.sade.osoitepalvelu.util.AssertUtil;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestAppConfig.class})
public class DefaultKoodistoServiceTest {
    private static final Locale LOCALE_FI  =  new Locale("fi", "FI");
    private static final String FI = "fi";
    //private static final String SV = "sv";
    private static final String EN = "en";

    @Autowired
    private DefaultKoodistoService koodistoService;

    @Before
    public void init() {
        KoodistoServiceRouteMock mock  =  new KoodistoServiceRouteMock();
        mock.add(versio(KoodistoTyyppi.OPPILAITOSTYYPPI, 0L), koodi("Yliopisto"));
        mock.add(versio(KoodistoTyyppi.OPPILAITOSTYYPPI, 1L),
                koodi("Yliopisto")
                        .add(arvo(FI, "Yliopisto"))
                        .add(arvo(EN, "Univerisity")),
                koodi("Ammattikoulu"));
        mock.add(versio(KoodistoTyyppi.OMISTAJATYYPPI, 1L), koodi("Testi"));
        mock.add(versio(KoodistoTyyppi.VUOSILUOKAT, 1L), koodi("Testi"));
        mock.add(versio(KoodistoTyyppi.MAAKUNTA, 1L),
                koodi("Ahvenanmaa"),
                koodi("Uusimaa"));
        mock.add(versio(KoodistoTyyppi.KUNTA, 1L), koodi("Helsinki"), koodi("Tampere"));
        mock.add(versio(KoodistoTyyppi.TUTKINTOTYYPPI, 1L), koodi("Toisen asteen ammatillinen koulutus"));
        mock.add(versio(KoodistoTyyppi.TUTKINTO, 1L), koodi("Maisterintutkinto"));
        mock.add(versio(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI, 1L), koodi("suomi"));
        mock.add(versio(KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA, 1L), koodi("suomi"), koodi("englanti"));
        mock.add(versio(KoodistoTyyppi.KOULUTUSASTEKELA, 1L), koodi("Testi"));
        mock.add(versio(KoodistoTyyppi.KOULUTUSTOIMIJA, 1L), koodi("Testi"));
        mock.add(versio(KoodistoTyyppi.OPINTOALAOPH2002, 1L), koodi("Testi"));
        mock.add(versio(KoodistoTyyppi.ALUEHALLINTOVIRASTO, 1L), koodi("Uudenmaan aluehallintovirasto"));
        mock.addKuntaByMaakunta("maakunta_12", versio(KoodistoTyyppi.KUNTA, 1L), koodi("Polvijärvi"), koodi("Kontiolahti"));

        koodistoService.setKoodistoRoute(mock);

        List<KayttooikesuryhmaDto> kayttoiskeusRyhmas = new ArrayList<KayttooikesuryhmaDto>();
        KayttooikesuryhmaDto kayttooikeusRyhma = new KayttooikesuryhmaDto();
        kayttooikeusRyhma.setId(10L);
        kayttooikeusRyhma.setName("Nimi");
        LocalizedContainerDto localizedName = new LocalizedContainerDto();
        List<LocalizedValueDto> texts = new ArrayList<LocalizedValueDto>();
        texts.add(new LocalizedValueDto("fi", "Nimi"));
        localizedName.setTexts(texts);
        kayttooikeusRyhma.setDescription(localizedName);
        kayttoiskeusRyhmas.add(kayttooikeusRyhma);
        koodistoService.setAuthenticationServiceRoute(new AuhenticationServiceRouteMock(kayttoiskeusRyhmas));

        OrganisaatioServiceRouteMock organisaatioMock = new OrganisaatioServiceRouteMock();
        OrganisaatioHierarchyResultsDto hierarchy = new OrganisaatioHierarchyResultsDto();
        hierarchy.setNumHits(5);
        List<OrganisaatioHierarchyDto> organisaatiot = new ArrayList<OrganisaatioHierarchyDto>();
        OrganisaatioHierarchyDto organisaatio = new OrganisaatioHierarchyDto();
        organisaatio.setYtunnus("1234856-7");
        organisaatio.setMatch(true);
        Map<String,String> nimi = new HashMap<String, String>();
        nimi.put("fi", "Testiorganisaatio");
        organisaatio.setNimi(nimi);
        organisaatiot.add(organisaatio);
        hierarchy.setOrganisaatiot(organisaatiot);
        organisaatioMock.setHierarchyResults(hierarchy);
        koodistoService.setOrganisaatioServiceRoute(organisaatioMock);
    }

    protected KoodistoVersioDto versio(KoodistoTyyppi tyyppi, long version) {
        return new KoodistoVersioDto(tyyppi.getUri(), tyyppi, version, new LocalDate(), new LocalDate(),
                KoodistoTila.LUONNOS);
    }

    protected KoodiDto koodi(String arvo) {
        return new KoodiDto(arvo);
    }

    protected KoodiArvoDto arvo(String locale, String arvo) {
        return new KoodiArvoDto(arvo, arvo, arvo, locale);
    }

    @Test
    public void testFindKayttooikeusryhmas() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findKayttooikeusryhmas(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KAYTTOOIKEUSRYHMA);
    }

    @Test
    public void testFindOppilaitosTyyppiValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findOppilaitosTyyppiOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOSTYYPPI);
    }

    @Test
    public void testFindOmistajaTyyppiValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findOmistajaTyyppiOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OMISTAJATYYPPI);
    }

    @Test
    public void testFindVuosiluokkaValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findVuosiluokkaOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.VUOSILUOKAT);
    }

    @Test
    public void testFindMaakuntaValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findMaakuntaOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.MAAKUNTA);
    }

    @Test
    public void testFindKuntaValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findKuntaOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KUNTA);
    }

    @Test
    public void testFindTutkintoTyyppiValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findTutkintoTyyppiOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTOTYYPPI);
    }

    @Test
    public void testFindTutkintoValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findTutkintoOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.TUTKINTO);
    }

    @Test
    public void testFindOppilaitoksenOpetuskieliValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findOppilaitoksenOpetuskieliOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI);
    }

    @Test
    public void testFindKoulutuksenKieliValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findKoulutuksenKieliOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA);
    }

    @Test
    public void testFindKoulutusAsteValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findKoulutusAsteOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSASTEKELA);
    }

    @Test
    public void testFindKoulutuksenJarjestejaValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findKoulutuksenJarjestejaOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.KOULUTUSTOIMIJA);
    }

    @Test
    public void testFindOpintoAlaValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findOpintoAlaOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.OPINTOALAOPH2002);
    }

    @Test
    public void testFindAlueHallintoVirastoValinnat() {
        List<UiKoodiItemDto> optiot  =  koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
        assertListNonEmptyAndItemsOfType(optiot, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
    }

    @Test
    public void testFindKuntasByMaakuntas() {
        List<UiKoodiItemDto> options  =  koodistoService.findKuntasByMaakuntaUri(LOCALE_FI, "maakunta_12");
        assertNotNull(options);
        assertTrue(options.size() > 0);
    }

    private static void assertListNonEmptyAndItemsOfType(List<UiKoodiItemDto> optiot, KoodistoTyyppi tyyppi) {
        AssertUtil.assertListNotEmpty(optiot, tyyppi.getUri());
        for (UiKoodiItemDto optio : optiot) {
            assertTrue("Virheellinen koodistotyyppi "  +  optio.getKoodistonTyyppi().name()  +  ", vaadittu arvo "
                    + tyyppi.name(), optio.getKoodistonTyyppi().equals(tyyppi));
        }
    }
}
