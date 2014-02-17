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
    private SearchTargetGroup.GroupType type;
    private List<SearchTargetGroup.TargetType> options = new ArrayList<SearchTargetGroup.TargetType>();

    public SearchTargetGroup.GroupType getType() {
        return type;
    }

    public void setType(SearchTargetGroup.GroupType type) {
        this.type = type;
    }

    public List<SearchTargetGroup.TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<SearchTargetGroup.TargetType> options) {
        this.options = options;
    }
}
