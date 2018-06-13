package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers.OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Iterator;
import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_LOCALE;

public class WwwOsoiteCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getWwwOsoite() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> elementtis =
                CollectionHelper.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", locale),
                        new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", DEFAULT_LOCALE))
                        .iterator();
        if (elementtis.hasNext()) {
            to.setWwwOsoite(elementtis.next().getArvo());
        }
        Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                CollectionHelper.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(locale),
                        new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(DEFAULT_LOCALE))
                        .iterator();
        if (yhteystietos.hasNext()) {
            to.setWwwOsoite(yhteystietos.next().getWww());
        }
    }
}
