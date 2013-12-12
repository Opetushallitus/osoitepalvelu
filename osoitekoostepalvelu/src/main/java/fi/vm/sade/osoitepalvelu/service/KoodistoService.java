package fi.vm.sade.osoitepalvelu.service;

import java.util.List;
import java.util.Locale;

import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;

public interface KoodistoService {

	List<UiKoodiItemDto> haeOppilaitosTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeOmistajaTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeVuosiluokkaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeMaakuntaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeKuntaValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeTutkintoTyyppiValinnat(Locale lokaali);
	
	List<UiKoodiItemDto> haeTutkintoValinnat(Locale lokaali);
}
