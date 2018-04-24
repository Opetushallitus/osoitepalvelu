package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.util.Locale;

public class OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private Locale locale;

    public OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto yhteystietoArvo) {
        return "Varhaiskasvatuksen yhteyshenkilö".equals(yhteystietoArvo.getTyyppiNimi())
                && yhteystietoArvo.getElementtiTyyppi().equals("Nimi")
                && yhteystietoArvo.getArvo() != null
                && LocaleHelper.languageEquals(locale,LocaleHelper.parseLocale(yhteystietoArvo.getKieli(),null ));
    }

}

