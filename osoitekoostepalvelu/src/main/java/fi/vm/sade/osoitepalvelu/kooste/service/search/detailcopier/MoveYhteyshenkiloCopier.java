package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import com.google.common.collect.Collections2;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

public class MoveYhteyshenkiloCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getMoveYhteyshenkilo() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                Collections2.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYksityiskohtainenYhteystietoByMoveYhteyshenkiloPredicate())
                        .iterator();
        if (yhteystietoArvos.hasNext()) {
            to.setMoveYhteyshenkilo(yhteystietoArvos.next().getArvo());
        }
    }
}
