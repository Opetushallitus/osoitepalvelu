package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.util.Locale;
import java.util.Set;

public class OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private final Set<String> kielet;

    public OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(Set<String> kielet) {
        this.kielet = kielet;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto yhteystietoArvo) {
        return "Varhaiskasvatuksen yhteyshenkilö".equals(yhteystietoArvo.getTyyppiNimi())
                && yhteystietoArvo.getElementtiTyyppi().equals("Nimi")
                && yhteystietoArvo.getArvo() != null
                && yhteystietoArvo.getKieli() != null
                && kielet.stream().anyMatch(kieli -> yhteystietoArvo.getKieli().startsWith(kieli));
    }

}

