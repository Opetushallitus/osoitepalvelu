package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

public class OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto input) {
        return "MOVE!-yhteyshenkilö/mittaustulosten syöttäjä".equals(input.getTyyppiNimi())
                && input.getArvo() != null;
    }

}
