package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.converter;

import fi.ratamaa.dtoconverter.DtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;

import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/17/13
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KoodistoDtoConverter extends DtoConverter {

    UiKoodiItemDto convert(KoodiDto koodi, UiKoodiItemDto uiKoodi, Locale lokaali);

    List<UiKoodiItemDto> convert(List<KoodiDto> koodit, List<UiKoodiItemDto> uiKoodit, Locale lokaali);

}
