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

import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTargetGroupDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SearchTermDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 2:51 PM
 */
@ApiModel("Kuvaa hakuehtoja")
public class SearchTermsDto implements Serializable {
    private static final long serialVersionUID  =  -6050192875078360323L;

    private static final int HASH_FACTOR = 31;

    public enum SearchType {
        EMAIL,
        SEND_LETTER,
        LETTER,
        CONTACT;
    }

    @ApiModelProperty("Lokalisointilokaali, jolla hakutulokset esitetään ja eri kieliset osoitteet " +
            " ja postitoimipaikkojen nimet pyritään valitsemaan.")
    private Locale locale;
    @ApiModelProperty("Haun käyttötarkoitus.")
    private SearchType searchType;
    @ApiModelProperty("Jos haku on CONTACT-käyttötarkoitukseen, ne osoitekentät, jotka halutaan sisällytettävän hakutuloksiin.")
    private List<String> addressFields  =  new ArrayList<String>();
    @ApiModelProperty("Haun kohderyhmät.")
    private List<SearchTargetGroupDto> targetGroups  =  new ArrayList<SearchTargetGroupDto>();
    @ApiModelProperty("Haun rajausehdot, jotka pääsääntöisesti ovat ja-suhteessa toisiinsa.")
    private List<SearchTermDto> terms  =  new ArrayList<SearchTermDto>();

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale  =  locale;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType  =  searchType;
    }

    public List<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(List<String> addressFields) {
        this.addressFields  =  addressFields;
    }

    public boolean containsAnyTargetGroup(SearchTargetGroup.GroupType[] groupTypes,
                                          SearchTargetGroup.TargetType... targetTypes) {
        for (SearchTargetGroupDto targetGroup : targetGroups) {
            for (SearchTargetGroup.GroupType type : groupTypes) {
                if (type == targetGroup.getType() && targetGroup.containsAnyOption(targetTypes)) {
                    return true;
                }
            }
        }
        return false;
    }

    public SearchTargetGroupDto getTargetGroup(SearchTargetGroup.GroupType groupType) {
        for (SearchTargetGroupDto targetGroup : targetGroups) {
            if (groupType == targetGroup.getType()) {
                return targetGroup;
            }
        }
        return null;
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

    public List<String> findTerms(String type) {
        for(SearchTermDto term : this.terms) {
            if(type.equals(term.getType())) {
                return term.getValues();
            }
        }
        return new ArrayList<String>();
    }

    public List<Boolean> findTermsAsBoolean(String type) {
        return this.terms.stream()
                .filter(term -> type.equals(term.getType()))
                .flatMap(term -> term.getValues().stream().map(Boolean::valueOf))
                .collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchTermsDto)) {
            return false;
        }
        SearchTermsDto that = (SearchTermsDto) o;

        if (addressFields != null ? !addressFields.equals(that.addressFields) : that.addressFields != null) {
            return false;
        }
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) {
            return false;
        }
        if (searchType != that.searchType) {
            return false;
        }
        if (targetGroups != null ? !targetGroups.equals(that.targetGroups) : that.targetGroups != null) {
            return false;
        }
        if (terms != null ? !terms.equals(that.terms) : that.terms != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = HASH_FACTOR * result + (searchType != null ? searchType.hashCode() : 0);
        result = HASH_FACTOR * result + (addressFields != null ? addressFields.hashCode() : 0);
        result = HASH_FACTOR * result + (targetGroups != null ? targetGroups.hashCode() : 0);
        result = HASH_FACTOR * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }
}