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

import java.io.Serializable;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 2:53 PM
 */
public interface SearchService extends Serializable {

    /**
     * @param terms to search with
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return results
     * @throws TooFewSearchConditionsForOrganisaatiosException if tehere are conditions for organisaatios other than
     * tyyppi (and organisaatios are returned by tyyppi or used as conditions for henkilös)
     * @throws TooFewSearchConditionsForHenkilosException if no conditions for henkilös or organisaatios and
     * henkilös are returned
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForKoulutusException
     */
    SearchResultsDto find(SearchTermsDto terms, CamelRequestContext requestContext)
            throws TooFewSearchConditionsForOrganisaatiosException,
                    TooFewSearchConditionsForHenkilosException,
                    TooFewSearchConditionsForKoulutusException;

}
