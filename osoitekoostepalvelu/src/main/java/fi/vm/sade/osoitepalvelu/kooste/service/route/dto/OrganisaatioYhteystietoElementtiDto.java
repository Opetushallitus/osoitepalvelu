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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/20/14
 * Time: 4:15 PM
 */
public class OrganisaatioYhteystietoElementtiDto implements Serializable {
    private static final long serialVersionUID = 3038635054979904682L;
    
    @JsonProperty("YhteystietoElementti.oid")
    private String elementtiOid;
    @JsonProperty("YhteystietoElementti.tyyppi")
    private String elementtiTyyppi;
    @JsonProperty("YhteystietojenTyyppi.oid")
    private String tyyppiOid;
    @JsonProperty("YhteystietoArvo.kieli")
    private String kieli;
    @JsonProperty("YhteystietoArvo.arvoText")
    private String arvo;

    public String getElementtiOid() {
        return elementtiOid;
    }

    public void setElementtiOid(String elementtiOid) {
        this.elementtiOid  =  elementtiOid;
    }

    public String getElementtiTyyppi() {
        return elementtiTyyppi;
    }

    public void setElementtiTyyppi(String elementtiTyyppi) {
        this.elementtiTyyppi  =  elementtiTyyppi;
    }

    public String getTyyppiOid() {
        return tyyppiOid;
    }

    public void setTyyppiOid(String tyyppiOid) {
        this.tyyppiOid  =  tyyppiOid;
    }

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli  =  kieli;
    }

    public String getArvo() {
        return arvo;
    }

    public void setArvo(String arvo) {
        this.arvo  =  arvo;
    }
}
