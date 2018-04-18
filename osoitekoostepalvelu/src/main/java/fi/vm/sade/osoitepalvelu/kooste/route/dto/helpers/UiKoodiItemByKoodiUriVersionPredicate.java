package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;

public class UiKoodiItemByKoodiUriVersionPredicate implements Predicate<UiKoodiItemDto> {

    private final String uri;

    public UiKoodiItemByKoodiUriVersionPredicate(String uriWithVersion) {
        this.uri = getUri(uriWithVersion);
    }

    private static String getUri(String uriWithVersion) {
        if (uriWithVersion == null) {
            return null;
        }
        int endIndex = uriWithVersion.indexOf("#");
        if (endIndex >= 0) {
            return uriWithVersion.substring(0, endIndex);
        }
        return uriWithVersion;
    }

    @Override
    public boolean apply(UiKoodiItemDto input) {
        return EqualsHelper.areEquals(uri, input.getKoodiUri());
    }

}
