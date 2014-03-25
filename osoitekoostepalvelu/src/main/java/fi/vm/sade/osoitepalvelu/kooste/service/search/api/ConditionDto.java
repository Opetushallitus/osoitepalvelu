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
import java.util.TreeSet;

public class ConditionDto implements Serializable {
    
    private static final long serialVersionUID  =  3474454227186986250L;

    private String operaattori;
    private TreeSet<SearchValueDto> ehdot;

    public ConditionDto() {
        this.ehdot  =  new TreeSet<SearchValueDto>();
    }
    
    public String getOperaattori() {
        return operaattori;
    }
    
    public void setOperaattori(String operaattori) {
        this.operaattori  =  operaattori;
    }

    public TreeSet<SearchValueDto> getEhdot() {
        return ehdot;
    }
    
    public void addEhto(SearchValueDto ehto) {
        this.ehdot.add(ehto);
    }   
}
