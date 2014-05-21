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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:34 PM
 */
@DtoConversion
public class SearchTargetGroupDto implements Serializable {
    private static final long serialVersionUID  =  191054363453587812L;

    private static final int HASH_FACTOR = 31;

    private SearchTargetGroup.GroupType type;
    private List<SearchTargetGroup.TargetType> options  =  new ArrayList<SearchTargetGroup.TargetType>();

    public SearchTargetGroup.GroupType getType() {
        return type;
    }

    public void setType(SearchTargetGroup.GroupType type) {
        this.type  =  type;
    }

    public List<SearchTargetGroup.TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<SearchTargetGroup.TargetType> options) {
        this.options  =  options;
    }

    public boolean containsAnyOption(SearchTargetGroup.TargetType... targetTypes) {
        if (targetTypes == null || targetTypes.length < 1) {
            return true;
        }
        for (SearchTargetGroup.TargetType type : targetTypes) {
            if (this.options.contains(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchTargetGroupDto)) {
            return false;
        }

        SearchTargetGroupDto that = (SearchTargetGroupDto) o;

        if (options != null ? !options.equals(that.options) : that.options != null) {
            return false;
        }
        if (type != that.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = HASH_FACTOR * result + (options != null ? options.hashCode() : 0);
        return result;
    }
}
