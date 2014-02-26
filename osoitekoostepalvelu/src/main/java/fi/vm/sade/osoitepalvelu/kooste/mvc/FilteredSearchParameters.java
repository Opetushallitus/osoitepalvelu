/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.mvc;

import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OidAndTyyppiPair;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;

import java.io.Serializable;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 4:34 PM
 */
public class FilteredSearchParameters implements Serializable {
    private static final long serialVersionUID = -8288132415175555661L;
    
    private SearchTermsDto searchTerms;
    private Set<OidAndTyyppiPair> nonIncludedOrganisaatioOids;

    public SearchTermsDto getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(SearchTermsDto searchTerms) {
        this.searchTerms = searchTerms;
    }

    public Set<OidAndTyyppiPair> getNonIncludedOrganisaatioOids() {
        return nonIncludedOrganisaatioOids;
    }

    public void setNonIncludedOrganisaatioOids(Set<OidAndTyyppiPair> nonIncludedOrganisaatioOids) {
        this.nonIncludedOrganisaatioOids = nonIncludedOrganisaatioOids;
    }
}
