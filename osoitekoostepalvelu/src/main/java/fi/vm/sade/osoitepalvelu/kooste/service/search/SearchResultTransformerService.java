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

package fi.vm.sade.osoitepalvelu.kooste.service.search;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto.SearchType;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:31 PM
 */
public interface SearchResultTransformerService extends Serializable {

    /**
     * @param results to be transformed
     * @param presentation holding the preferred locale to filter positosoites with
     * @param requestContext the context for HTTP request received by the application to operate in
     * @param searchType the type of the search
     * @return the results, rows for presentation
     */
    SearchResultsPresentationDto transformToResultRows(SearchResultsDto results,
                                                       SearchResultPresentation presentation,
                                                       CamelRequestContext requestContext,
                                                       SearchType searchType);



    /**
     * @return the currently logged in user's OID.
     */
    String getLoggeInUserOid();
}
