package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_LOCALE;

public class VarhaiskasvatuksenYhteyshenkiloCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) { return from.getVarhaiskasvatuksenYhteyshenkilo() == null; }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                CollectionHelper.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(locale),
                        new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenYhteyshenkiloPredicate(DEFAULT_LOCALE))
                        .iterator();
        if(yhteystietoArvos.hasNext()) {
            to.setVarhaiskasvatuksenYhteyshenkilo(yhteystietoArvos.next().getArvo());
        }
    }
}
