package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_LOCALE;

public class VarhaiskasvatuksenEmailCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) { return from.getVarhaiskasvatuksenEmail() == null; }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                CollectionHelper.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate(locale),
                        new OrganisaatioYksityiskohtainenYhteystietoByVarhaiskasvatuksenEmailPredicate(DEFAULT_LOCALE))
                        .iterator();
        if(yhteystietoArvos.hasNext()) {
            to.setVarhaiskasvatuksenEmail(yhteystietoArvos.next().getArvo());
        }
    }
}
