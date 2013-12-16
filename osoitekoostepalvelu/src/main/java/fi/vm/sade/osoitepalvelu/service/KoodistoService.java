package fi.vm.sade.osoitepalvelu.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;

/**
 * Service -rajapinta, jonka kautta pystyy hakemaan koodiston arvoja koodiston
 * tyypin perusteella. Oletusarvoisesti tämä rajapinta hakee kaikista koodeista
 * viimeisimmän voimassa olevan version.
 */
public interface KoodistoService {

	List<UiKoodiItemDto> haeOppilaitosTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeOmistajaTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeVuosiluokkaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeMaakuntaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeKuntaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeTutkintoTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeTutkintoValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeOppilaitoksenOpetuskieliValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeKoulutuksenKieliValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeKoulutusAsteValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeKoulutuksenJarjestejaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeOpintoAlaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeAlueHallintoVirastoValinnat(Locale lokaali);
	
	/**
	 * Hakee kaikkien koodistojen arvot yhdellä kertaa ja paulattaa näistä mapin.
	 * Mapista halutun koodiston voi hakea KoodistoTyyppi enumin arvon
	 * perustella. Huom: Metodi suorittaa useita kutsuja ulkoiseen koodistopalveluun.
	 * 
	 * @param lokaali Koodistojen arvojen nimien ja kuvauksen lokalisoinnin lokaali.
	 * @return Mappi, joka sisältää kaikkien tuettujen koodistojen arvot.
	 */
	Map<KoodistoTyyppi, List<UiKoodiItemDto>> haeKaikkiTuetutKoodistot(Locale lokaali);
}
