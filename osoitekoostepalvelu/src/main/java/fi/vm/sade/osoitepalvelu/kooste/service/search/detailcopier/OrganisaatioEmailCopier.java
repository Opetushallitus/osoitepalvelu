package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.OpetuskieliHelper.opetuskieletToKielet;
import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_OPETUSKIELI;
import static java.util.Collections.singleton;

public class OrganisaatioEmailCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getEmailOsoite() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                CollectionHelper.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(opetuskieletToKielet(from.getKieletUris())),
                        new OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(singleton(DEFAULT_OPETUSKIELI)))
                        .iterator();
        if (yhteystietos.hasNext()) {
            to.setEmailOsoite(yhteystietos.next().getEmail());
        }
    }
}
