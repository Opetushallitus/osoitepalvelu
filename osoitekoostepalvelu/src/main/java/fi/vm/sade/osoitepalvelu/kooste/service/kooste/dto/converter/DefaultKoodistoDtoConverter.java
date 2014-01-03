package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.converter;

import java.util.List;
import java.util.Locale;

import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodiArvoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;

@Component
public class DefaultKoodistoDtoConverter extends AbstractDtoConverter implements KoodistoDtoConverter {

    public UiKoodiItemDto convert(KoodiDto koodi, UiKoodiItemDto uiKoodi, Locale lokaali) {
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

    public List<UiKoodiItemDto> convert(List<KoodiDto> koodit, List<UiKoodiItemDto> uiKoodit, Locale lokaali) {
        for (KoodiDto koodi : koodit) {
            UiKoodiItemDto optio = convert(koodi, new UiKoodiItemDto(), lokaali);
            uiKoodit.add(optio);
        }
        return uiKoodit;
    }
}
