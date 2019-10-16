package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.util.Set;

public class OrganisaatioYksityiskohtainenYhteystietoByKoskiYhdyshenkiloPredicate implements Predicate<OrganisaatioYhteystietoElementtiDto> {

    private final Set<String> kielet;

    public OrganisaatioYksityiskohtainenYhteystietoByKoskiYhdyshenkiloPredicate(Set<String> kielet) {
        this.kielet = kielet;
    }

    @Override
    public boolean apply(OrganisaatioYhteystietoElementtiDto yhteystietoArvo) {
        return "KOSKI-palvelun omien tietojen virheilmoituksen sähköpostiosoite".equals(yhteystietoArvo.getTyyppiNimi())
                && yhteystietoArvo.getArvo() != null
                && yhteystietoArvo.getKieli() != null
                && kielet.stream().anyMatch(kieli -> yhteystietoArvo.getKieli().startsWith(kieli));
    }
}
