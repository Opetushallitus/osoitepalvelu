package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.OpetuskieliHelper.opetuskieletToKielet;
import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_OPETUSKIELI;
import static java.util.Collections.singleton;

public class PuhelinnumeroCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getPuhelinnumero() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                CollectionHelper.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(opetuskieletToKielet(from.getKieletUris())),
                        new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(singleton(DEFAULT_OPETUSKIELI)))
                        .iterator();
        if (yhteystietos.hasNext()) {
            to.setPuhelinnumero(yhteystietos.next().getNumero());
        }
    }
}
