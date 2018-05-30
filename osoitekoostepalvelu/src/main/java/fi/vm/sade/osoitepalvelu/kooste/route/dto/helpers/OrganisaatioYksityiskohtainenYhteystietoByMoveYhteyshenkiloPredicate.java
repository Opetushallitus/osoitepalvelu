package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import java.util.Locale;

public class OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private final Locale locale;

    public OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto input) {
        return "MOVE!-yhteyshenkilö/mittaustulosten syöttäjä".equals(input.getTyyppiNimi())
                && input.getArvo() != null
                && LocaleHelper.languageEquals(locale, LocaleHelper.parseLocale(input.getKieli(), null));
    }

}
