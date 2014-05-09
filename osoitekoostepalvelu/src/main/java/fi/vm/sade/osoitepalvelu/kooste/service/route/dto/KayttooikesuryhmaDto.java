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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 3:02 PM
 */
public class KayttooikesuryhmaDto implements Serializable {
    private static final long serialVersionUID = 4868926511774703941L;
    
    private Long id;
    @DtoConversion(path = "koodiId")
    private String name;
    @DtoConversion(path = "nimi")
    private LocalizedContainerDto description  =  new LocalizedContainerDto();
    private List<OrganisaatioviiteDto> organisaatioViite  =  new ArrayList<OrganisaatioviiteDto>();
    private Object rooliRajoite; /// TODO? what does this contain?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id  =  id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name  =  name;
    }

    public LocalizedContainerDto getDescription() {
        return description;
    }

    public void setDescription(LocalizedContainerDto description) {
        this.description  =  description;
    }

    public List<OrganisaatioviiteDto> getOrganisaatioViite() {
        return organisaatioViite;
    }

    public void setOrganisaatioViite(List<OrganisaatioviiteDto> organisaatioViite) {
        this.organisaatioViite  =  organisaatioViite;
    }

    public Object getRooliRajoite() {
        return rooliRajoite;
    }

    public void setRooliRajoite(Object rooliRajoite) {
        this.rooliRajoite  =  rooliRajoite;
    }
}
