package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.util.Set;

public class OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private final Set<String> kielet;

    public OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate(Set<String> kielet) {
        this.kielet = kielet;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto input) {
        return "MOVE!-yhteyshenkilö/mittaustulosten syöttäjä".equals(input.getTyyppiNimi())
                && input.getArvo() != null
                && input.getKieli() != null
                && kielet.stream().anyMatch(kieli -> input.getKieli().startsWith(kieli));
    }

}
