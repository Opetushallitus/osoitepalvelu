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

public class OrganisaatioYhteystietoDto implements Serializable {
    private static final long serialVersionUID  =  -4697143053873590608L;

    private String yhteyshenkiloOid;
    private String nimi;
    private String nimike; //Kuten Rehtori
    private String email;
    
    public String getNimi() {
        return nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi  =  nimi;
    }
    
    public String getNimike() {
        return nimike;
    }
    
    public void setNimike(String nimike) {
        this.nimike  =  nimike;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email  =  email;
    }

    public String getYhteyshenkiloOid() {
        return yhteyshenkiloOid;
    }

    public void setYhteyshenkiloOid(String yhteyshenkiloOid) {
        this.yhteyshenkiloOid  =  yhteyshenkiloOid;
    }
}
