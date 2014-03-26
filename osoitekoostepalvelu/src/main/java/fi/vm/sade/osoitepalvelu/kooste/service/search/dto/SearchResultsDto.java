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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 3:26 PM
 */
public class SearchResultsDto implements Serializable {
    private List<HenkiloHakuResultDto> henkilos = new ArrayList<HenkiloHakuResultDto>();
    private List<OrganisaatioResultDto> organisaatios = new ArrayList<OrganisaatioResultDto>();

    public List<HenkiloHakuResultDto> getHenkilos() {
        return henkilos;
    }

    public void setHenkilos(List<HenkiloHakuResultDto> henkilos) {
        this.henkilos = henkilos;
    }

    public List<OrganisaatioResultDto> getOrganisaatios() {
        return organisaatios;
    }

    public void setOrganisaatios(List<OrganisaatioResultDto> organisaatios) {
        this.organisaatios = organisaatios;
    }
}
