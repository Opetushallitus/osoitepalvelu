package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UiKoodiItemByKoodiUriVersionPredicateTest {

    private static UiKoodiItemDto uiKoodiItemDto(String koodiUri) {
        UiKoodiItemDto uiKoodiItemDto = new UiKoodiItemDto();
        uiKoodiItemDto.setKoodiUri(koodiUri);
        return uiKoodiItemDto;
    }

    @Test
    public void withoutVersion() {
        UiKoodiItemByKoodiUriVersionPredicate predicate = new UiKoodiItemByKoodiUriVersionPredicate("maakunta_1");
        assertTrue(predicate.apply(uiKoodiItemDto("maakunta_1")));
        assertFalse(predicate.apply(uiKoodiItemDto("maakunta_11")));
        assertFalse(predicate.apply(uiKoodiItemDto("maakunta_2")));
    }

    @Test
    public void withVersion() {
        UiKoodiItemByKoodiUriVersionPredicate predicate = new UiKoodiItemByKoodiUriVersionPredicate("oppilaitoksenopetuskieli_1#1");
        assertTrue(predicate.apply(uiKoodiItemDto("oppilaitoksenopetuskieli_1")));
        assertFalse(predicate.apply(uiKoodiItemDto("oppilaitoksenopetuskieli_11")));
        assertFalse(predicate.apply(uiKoodiItemDto("oppilaitoksenopetuskieli_2")));
    }

    @Test
    public void withVersionOnly() {
        UiKoodiItemByKoodiUriVersionPredicate predicate = new UiKoodiItemByKoodiUriVersionPredicate("#1");
        assertTrue(predicate.apply(uiKoodiItemDto("")));
        assertFalse(predicate.apply(uiKoodiItemDto("1")));
    }

    @Test
    public void withNull() {
        UiKoodiItemByKoodiUriVersionPredicate predicate = new UiKoodiItemByKoodiUriVersionPredicate(null);
        assertTrue(predicate.apply(uiKoodiItemDto(null)));
        assertFalse(predicate.apply(uiKoodiItemDto("null_1")));
    }

}
