package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.util.Set;

public class OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private Set<String> kielet;

    public OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate(Set<String> kielet) {
        this.kielet = kielet;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto yhteystietoArvo) {
        return "Varhaiskasvatuksen yhteyshenkilö".equals(yhteystietoArvo.getTyyppiNimi())
                && yhteystietoArvo.getElementtiTyyppi().equals("Email")
                && yhteystietoArvo.getArvo() != null
                && yhteystietoArvo.getKieli() != null
                && kielet.stream().anyMatch(kieli -> yhteystietoArvo.getKieli().startsWith(kieli));
    }

}
