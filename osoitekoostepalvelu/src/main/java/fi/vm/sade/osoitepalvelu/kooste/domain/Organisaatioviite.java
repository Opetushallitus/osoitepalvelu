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

package fi.vm.sade.osoitepalvelu.kooste.domain;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 10:11 AM
 */
public class Organisaatioviite implements Serializable {
    private static final long serialVersionUID = 7965399008018056620L;
    
    private Long id;
    private String organisaatioOid;
    private boolean includeChildren = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id  =  id;
    }

    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid  =  organisaatioOid;
    }

    public boolean isIncludeChildren() {
        return includeChildren;
    }

    public void setIncludeChildren(boolean includeChildren) {
        this.includeChildren  =  includeChildren;
    }
}
