package fi.vm.sade.osoitepalvelu.kooste.service.kooste;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;

/**
 * Service -rajapinta, jonka kautta pystyy hakemaan koodiston arvoja koodiston
 * tyypin perusteella. Oletusarvoisesti tämä rajapinta hakee kaikista koodeista
 * viimeisimmän voimassa olevan version.
 */
public interface KoodistoService {

    List<UiKoodiItemDto> findOppilaitosTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findOmistajaTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findVuosiluokkaOptions(Locale locale);

    List<UiKoodiItemDto> findMaakuntaOptions(Locale locale);

    List<UiKoodiItemDto> findKuntaOptions(Locale locale);

    List<UiKoodiItemDto> findTutkintoTyyppiOptions(Locale locale);

    List<UiKoodiItemDto> findTutkintoOptions(Locale locale);

    List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutuksenKieliOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutusAsteOptions(Locale locale);

    List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(Locale locale);

    List<UiKoodiItemDto> findOpintoAlaOptions(Locale locale);

    List<UiKoodiItemDto> findAlueHallintoVirastoOptions(Locale locale);

    /**
     * Hakee kaikkien koodistojen arvot yhdellä kertaa ja paulattaa näistä
     * mapin. Mapista halutun koodiston voi hakea KoodistoTyyppi enumin arvon
     * perustella. Huom: Metodi suorittaa useita kutsuja ulkoiseen
     * koodistopalveluun.
     * 
     * @param locale
     *            Koodistojen arvojen nimien ja kuvauksen lokalisoinnin locale.
     * @return Mappi, joka sisältää kaikkien tuettujen koodistojen arvot.
     */
    Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllKoodistos(Locale locale);
}
