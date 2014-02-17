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

import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:31 PM
 */
public interface SearchResultTransformerService {

    /**
     * @param results to be aggregated
     * @param presentation holding the preferred locale to filter positosoites with
     * @return the aggregated rows for presentation (in view or excel, for example)
     */
    List<SearchResultRowDto> aggregateResultRows( List<OrganisaatioResultDto> results,
                                                  SearchResultPresentation presentation );

    /**
     * @param workbook the excel workbook to produce the rows to
     * @param rows the rows for the excel
     * @param presentation holding e.g. the preferred localization locale and columns to include in the excel
     */
    void produceExcel(Workbook workbook, List<SearchResultRowDto> rows, SearchResultPresentation presentation);

    /**
     * @return the currently logged in user's OID.
     */
    String getLoggeInUserOid();
}
