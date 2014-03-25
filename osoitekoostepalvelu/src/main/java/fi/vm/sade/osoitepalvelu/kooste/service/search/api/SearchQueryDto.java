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

package fi.vm.sade.osoitepalvelu.kooste.service.search.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchQueryDto implements Serializable {

    private static final long serialVersionUID  =  1078154058705560606L;
    
    private Map<String, ConditionDto> taiEhdot  =  new HashMap<String, ConditionDto>();
    private Map<String, ConditionDto> jaEhdot  =  new HashMap<String, ConditionDto>();
    
    public SearchQueryDto() {
    }
    
    public Map<String, ConditionDto> getTaiEhdot() {
        return taiEhdot;
    }
    
    public Map<String, ConditionDto> getJaEhdot() {
        return jaEhdot;
    }
    
    public void addAndCriteria(SearchKeyDto keyDto, SearchValueDto value) {

        if (!jaEhdot.containsKey(keyDto.getKoodistoUri())) {
            ConditionDto dto  =  new ConditionDto();
            dto.setOperaattori(keyDto.getOperaattori());
            jaEhdot.put(keyDto.getKoodistoUri(), dto);
        }

        jaEhdot.get(keyDto.getKoodistoUri()).addEhto(value);
    }
    
    public void addOrCriteria(SearchKeyDto keyDto, SearchValueDto value) {

        if (!taiEhdot.containsKey(keyDto.getKoodistoUri())) {
            ConditionDto dto  =  new ConditionDto();
            dto.setOperaattori(keyDto.getOperaattori());
            taiEhdot.put(keyDto.getKoodistoUri(), dto);
        }

        taiEhdot.get(keyDto.getKoodistoUri()).addEhto(value);
    }    
}
