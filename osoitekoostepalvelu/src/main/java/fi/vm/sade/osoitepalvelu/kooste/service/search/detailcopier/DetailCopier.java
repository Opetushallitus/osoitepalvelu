package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Locale;

public interface DetailCopier {
    boolean isMissing(SearchResultRowDto from);
    void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale);
}
