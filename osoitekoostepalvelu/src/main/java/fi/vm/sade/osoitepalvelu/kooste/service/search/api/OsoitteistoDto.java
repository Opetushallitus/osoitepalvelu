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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;

import java.io.Serializable;

public class OsoitteistoDto implements Serializable {

    private static final long serialVersionUID  =  6985560274560979066L;
    
    private String kieli;
    @DtoConversion(withClass  =  OrganisaatioYhteysosoiteDto.class)
    private String osoiteTyyppi;
    private String yhteystietoOid;
    private String osoite;
    private String postilokero;
    private String postinumero;
    private String postitoimipaikka;
    private String extraRivi;

    public String getOsoiteTyyppi() {
        return osoiteTyyppi;
    }

    public void setOsoiteTyyppi(String osoiteTyyppi) {
        this.osoiteTyyppi  =  osoiteTyyppi;
    }

    public String getYhteystietoOid() {
        return yhteystietoOid;
    }

    public void setYhteystietoOid(String yhteystietoOid) {
        this.yhteystietoOid  =  yhteystietoOid;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite  =  osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero  =  postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka  =  postitoimipaikka;
    }

    public String getExtraRivi() {
        return extraRivi;
    }

    public void setExtraRivi(String extraRivi) {
        this.extraRivi  =  extraRivi;
    }
    
    public String getKieli() {
        return kieli;
    }
    
    public void setKieli(String kieli) {
        this.kieli  =  kieli;
    }

    public String getPostilokero() {
        return postilokero;
    }

    public void setPostilokero(String postilokero) {
        this.postilokero  =  postilokero;
    }
}
