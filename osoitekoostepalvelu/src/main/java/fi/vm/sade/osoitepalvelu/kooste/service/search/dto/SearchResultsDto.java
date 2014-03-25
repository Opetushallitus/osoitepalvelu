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

package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 2/18/14
 * Time: 9:45 AM
 */
public class SearchResultsDto implements Serializable {
    private static final long serialVersionUID  =  -8360101698155316476L;
    
    private List<SearchResultRowDto> rows;
    private SearchResultPresentation presentation;

    public SearchResultsDto() {
    }

    public SearchResultsDto(List<SearchResultRowDto> rows, SearchResultPresentation presentation) {
        this.rows  =  rows;
        this.presentation  =  presentation;
    }

    public List<SearchResultRowDto> getRows() {
        return rows;
    }

    public void setRows(List<SearchResultRowDto> rows) {
        this.rows  =  rows;
    }

    public SearchResultPresentation getPresentation() {
        return presentation;
    }

    public void setPresentation(SearchResultPresentation presentation) {
        this.presentation  =  presentation;
    }
}
