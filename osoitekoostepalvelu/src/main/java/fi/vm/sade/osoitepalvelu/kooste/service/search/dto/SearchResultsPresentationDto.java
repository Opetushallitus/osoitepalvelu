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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApiModel("Hakutulokset")
public class SearchResultsPresentationDto implements Serializable {
    private static final long serialVersionUID  =  -8360101698155316476L;

    @ApiModelProperty("Hakutulosten rivit")
    private List<SearchResultRowDto> rows;
    @ApiModelProperty("Hakutulosten esitystapa")
    private SearchResultPresentation presentation;
    @ApiModelProperty("Lähderekisterit")
    private Set<SourceRegister> sourceRegisters = new HashSet<SourceRegister>();

    public SearchResultsPresentationDto() {
    }

    public SearchResultsPresentationDto(List<SearchResultRowDto> rows, SearchResultPresentation presentation) {
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

    public Set<SourceRegister> getSourceRegisters() {
        return sourceRegisters;
    }

    public void setSourceRegisters(Set<SourceRegister> sourceRegisters) {
        this.sourceRegisters = sourceRegisters;
    }
}
