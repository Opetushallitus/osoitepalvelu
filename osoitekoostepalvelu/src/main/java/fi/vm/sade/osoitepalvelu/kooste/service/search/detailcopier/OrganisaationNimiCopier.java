package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.service.AbstractService.DEFAULT_LOCALE;

public class OrganisaationNimiCopier implements DetailCopier {
    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getNimi() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        to.setNimi(LocaleHelper.localized(from.getNimi(), locale, DEFAULT_LOCALE));
    }
}
