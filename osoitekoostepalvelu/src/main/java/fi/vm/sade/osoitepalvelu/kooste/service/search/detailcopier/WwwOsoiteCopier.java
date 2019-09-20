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

import static fi.vm.sade.osoitepalvelu.kooste.common.util.OpetuskieliHelper.opetuskieletToKielet;
import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_OPETUSKIELI;
import static java.util.Collections.singleton;

public class WwwOsoiteCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getWwwOsoite() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        Iterator<OrganisaatioYhteystietoElementtiDto> elementtis =
                CollectionHelper.filter(from.getYhteystietoArvos(),
                        new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", opetuskieletToKielet(from.getKieletUris())),
                        new OrganisaatioYhteystietoElementtiByElementtiTyyppiAndKieliPreidcate("Www", singleton(DEFAULT_OPETUSKIELI)))
                        .iterator();
        if (elementtis.hasNext()) {
            to.setWwwOsoite(elementtis.next().getArvo());
        }
        Iterator<OrganisaatioDetailsYhteystietoDto> yhteystietos =
                CollectionHelper.filter(from.getYhteystiedot(),
                        new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(opetuskieletToKielet(from.getKieletUris())),
                        new OrganisaatioYksityiskohtainenYhteystietoByWwwPredicate(singleton(DEFAULT_OPETUSKIELI)))
                        .iterator();
        if (yhteystietos.hasNext()) {
            to.setWwwOsoite(yhteystietos.next().getWww());
        }
    }
}
