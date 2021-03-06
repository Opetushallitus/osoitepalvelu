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

package fi.vm.sade.osoitepalvelu.kooste.route.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/27/14
 * Time: 1:35 PM
 */
public class OrganisaatioHierarchyDto implements Serializable {
    private static final long serialVersionUID = -4472354752997371430L;
    
    private String oid;
    private String parentId;
    @DtoConversion(withClass = UiKoodiItemDto.class, path = "koodiId")
    private String ytunnus;
    private boolean match;
    @DtoConversion(skip = true)
    private Map<String,String> nimi = new HashMap<String, String>();
    private List<OrganisaatioHierarchyDto> children = new ArrayList<OrganisaatioHierarchyDto>();

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public List<OrganisaatioHierarchyDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrganisaatioHierarchyDto> children) {
        this.children = children;
    }
}
