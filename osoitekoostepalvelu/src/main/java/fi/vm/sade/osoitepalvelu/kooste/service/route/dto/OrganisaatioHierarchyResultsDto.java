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

package fi.vm.sade.osoitepalvelu.kooste.service.route.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/27/14
 * Time: 1:34 PM
 */
public class OrganisaatioHierarchyResultsDto implements Serializable {
    private static final long serialVersionUID = -1619343744450175180L;
    
    private int numHits;
    private List<OrganisaatioHierarchyDto> organisaatiot = new ArrayList<OrganisaatioHierarchyDto>();

    public int getNumHits() {
        return numHits;
    }

    public void setNumHits(int numHits) {
        this.numHits = numHits;
    }

    public List<OrganisaatioHierarchyDto> getOrganisaatiot() {
        return organisaatiot;
    }

    public void setOrganisaatiot(List<OrganisaatioHierarchyDto> organisaatiot) {
        this.organisaatiot = organisaatiot;
    }
}
