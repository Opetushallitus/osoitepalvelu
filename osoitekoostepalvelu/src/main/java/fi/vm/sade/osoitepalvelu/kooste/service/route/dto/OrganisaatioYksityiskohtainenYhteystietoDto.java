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

/**
 * User: ratamaa
 * Date: 3/20/14
 * Time: 4:33 PM
 */
public class OrganisaatioYksityiskohtainenYhteystietoDto implements Serializable {
    private String osoiteTyyppi; // esim. posti, kaynti, puhein, null, ...
    private String yhteystietoOid;
    private String kieli; // esim. kieli_fi#1
    private String osoite;
    private String postitoimipaikka;
    private String postinumeroUri;
    private String extraRivi;
    private String maaUri;
    private String email;
    private String numero;
    private String www;

    public String getOsoiteTyyppi() {
        return osoiteTyyppi;
    }

    public void setOsoiteTyyppi(String osoiteTyyppi) {
        this.osoiteTyyppi = osoiteTyyppi;
    }

    public String getYhteystietoOid() {
        return yhteystietoOid;
    }

    public void setYhteystietoOid(String yhteystietoOid) {
        this.yhteystietoOid = yhteystietoOid;
    }

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli = kieli;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public String getPostinumeroUri() {
        return postinumeroUri;
    }

    public void setPostinumeroUri(String postinumeroUri) {
        this.postinumeroUri = postinumeroUri;
    }

    public String getExtraRivi() {
        return extraRivi;
    }

    public void setExtraRivi(String extraRivi) {
        this.extraRivi = extraRivi;
    }

    public String getMaaUri() {
        return maaUri;
    }

    public void setMaaUri(String maaUri) {
        this.maaUri = maaUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
