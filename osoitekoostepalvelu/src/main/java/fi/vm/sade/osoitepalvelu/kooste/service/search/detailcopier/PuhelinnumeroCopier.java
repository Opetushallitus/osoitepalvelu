package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_LOCALE;

public class PuhelinnumeroCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getPuhelinnumero() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                CollectionHelper.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(locale),
                        new OrganisaatioYksityiskohtainenYhteystietoByPuhelinPreidcate(DEFAULT_LOCALE))
                        .iterator();
        if (yhteystietos.hasNext()) {
            to.setPuhelinnumero(yhteystietos.next().getNumero());
        }
    }
}
