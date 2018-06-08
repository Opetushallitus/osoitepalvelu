package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OrganisaationSijaintikuntaCopier implements DetailCopier {

    @Autowired
    private KoodistoService koodistoService;

    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getKotikunta() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        if (from.getKotipaikkaUri() != null) {
            UiKoodiItemDto kuntaKoodi = koodistoService.findKuntaByKoodiUri(locale, from.getKotipaikkaUri());
            if (kuntaKoodi != null) {
                to.setKotikunta(kuntaKoodi.getNimi());
            }
        }
    }

}
