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

import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioTiedotDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:31 PM
 */
public interface SearchResultTransformerService {

    /**
     * Creates a scalar product of given orgranisaatios, their yhteyshenkilos and postiosoites.
     * Filters the postiosoites to match the possibly given locale of the presentation and localizes orgnisaatio's
     * nimi with the same locale.
     *
     * @param results to be transformed
     * @param presentation holding the preferred locale to filter positosoites with
     * @return the results, rows for presentation
     */
    SearchResultsDto transformToResultRows(List<OrganisaatioTiedotDto> results,
                                                   SearchResultPresentation presentation);

    /**
     * @param workbook the excel workbook to produce the rows to
     * @param searchResults to produce to the workbook
     */
    void produceExcel(Workbook workbook, SearchResultsDto searchResults);

    /**
     * @return the currently logged in user's OID.
     */
    String getLoggeInUserOid();
}
