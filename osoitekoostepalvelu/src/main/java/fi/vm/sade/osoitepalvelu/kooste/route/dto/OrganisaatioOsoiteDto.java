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

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/20/14
 * Time: 4:18 PM
 */
public class OrganisaatioOsoiteDto implements Serializable {
    private static final long serialVersionUID = 5029563025955715983L;
    
    private OrganisaatioYhteysosoiteDto.OsoiteTyyppi osoiteTyyppi;
    private String yhteystietoOid;
    private String postinumeroUri;
    private String osoite;
    private String postitoimipaikka;
    private String extraRivi;

    public OrganisaatioYhteysosoiteDto.OsoiteTyyppi getOsoiteTyyppi() {
        return osoiteTyyppi;
    }

    public void setOsoiteTyyppi(OrganisaatioYhteysosoiteDto.OsoiteTyyppi osoiteTyyppi) {
        this.osoiteTyyppi  =  osoiteTyyppi;
    }

    public String getYhteystietoOid() {
        return yhteystietoOid;
    }

    public void setYhteystietoOid(String yhteystietoOid) {
        this.yhteystietoOid  =  yhteystietoOid;
    }

    public String getPostinumeroUri() {
        return postinumeroUri;
    }

    public void setPostinumeroUri(String postinumeroUri) {
        this.postinumeroUri  =  postinumeroUri;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite  =  osoite;
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
}
