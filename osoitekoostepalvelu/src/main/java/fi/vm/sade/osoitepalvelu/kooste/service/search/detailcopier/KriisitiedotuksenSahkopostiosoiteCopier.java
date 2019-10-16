package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoArvoByKriisiEmailPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.OpetuskieliHelper.opetuskieletToKielet;
import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_OPETUSKIELI;
import static java.util.Collections.singleton;


public class KriisitiedotuksenSahkopostiosoiteCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getKriisitiedotuksenEmail() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos =
                CollectionHelper.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYksityiskohtainenYhteystietoArvoByKriisiEmailPredicate(opetuskieletToKielet(from.getKieletUris())),
                        new OrganisaatioYksityiskohtainenYhteystietoArvoByKriisiEmailPredicate(singleton(DEFAULT_OPETUSKIELI)))
                        .iterator();
        if(yhteystietoArvos.hasNext()) {
            to.setKriisitiedotuksenEmail(yhteystietoArvos.next().getArvo());
        }
    }
}
