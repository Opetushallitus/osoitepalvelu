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

package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoNotImported;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:35 PM
 */
@DtoConversion
@DtoNotImported
@ApiModel("Tallennettavan haun tiedot")
public class SavedSearchSaveDto {
    @ApiModelProperty("Haun nimi")
    private String name;
    @ApiModelProperty("Haun tyyppi")
    private SavedSearch.SaveType searchType;
    @ApiModelProperty("Haun ollessa CONTACT-tyyppinen, ne osoitekentät, jotka hakutuloksiin sisällytetään.")
    private List<String> addressFields  =  new ArrayList<String>();
    @ApiModelProperty("Haun kohderyhmät")
    private List<SearchTargetGroupDto> targetGroups  =  new ArrayList<SearchTargetGroupDto>();
    @ApiModelProperty("Haun pääasiassa ja-tyyppiset rajausehdot")
    private List<SearchTermDto> terms  =  new ArrayList<SearchTermDto>();
    @ApiModelProperty("Haun esitysmuodossa käytetty lokalisointilokaali: fi|sv|en")
    private String lang;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name  =  name;
    }

    public SavedSearch.SaveType getSearchType() {
        return searchType;
    }

    public void setSearchType(SavedSearch.SaveType searchType) {
        this.searchType  =  searchType;
    }

    public List<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(List<String> addressFields) {
        this.addressFields  =  addressFields;
    }

    public List<SearchTargetGroupDto> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<SearchTargetGroupDto> targetGroups) {
        this.targetGroups  =  targetGroups;
    }

    public List<SearchTermDto> getTerms() {
        return terms;
    }

    public void setTerms(List<SearchTermDto> terms) {
        this.terms  =  terms;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
