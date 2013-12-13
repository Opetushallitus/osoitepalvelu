package fi.vm.sade.osoitepalvelu.service.converter;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.service.dto.KoodiArvoDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;

@Component
public class KoodistoDtoConverter {
	
	public UiKoodiItemDto convert(KoodiDto koodi, Locale lokaali, UiKoodiItemDto uiKoodi) {
		uiKoodi.setKoodiId(koodi.getKoodiArvo());
		uiKoodi.setKoodistonTyyppi(koodi.getKoodisto().getTyyppi());
		
		KoodiArvoDto arvo = koodi.getArvoByKieli(lokaali.getLanguage());
		if (arvo != null) {
			uiKoodi.setNimi(arvo.getNimi());
			uiKoodi.setKuvaus(arvo.getKuvaus());
			uiKoodi.setLyhytNimi(arvo.getLyhytNimi());
		} else {
			uiKoodi.setNimi(lokaali.getLanguage() + "_missing!");			
			uiKoodi.setLyhytNimi(lokaali.getLanguage() + "_missing!");
		}
		return uiKoodi;
	}
	 
	public List<UiKoodiItemDto> convert(List<KoodiDto> koodit, Locale lokaali, List<UiKoodiItemDto> uiKoodit) {
		for (KoodiDto koodi : koodit) {
			UiKoodiItemDto optio = convert(koodi, lokaali, new UiKoodiItemDto());
			uiKoodit.add(optio);
		}
		return uiKoodit;
	}
}
