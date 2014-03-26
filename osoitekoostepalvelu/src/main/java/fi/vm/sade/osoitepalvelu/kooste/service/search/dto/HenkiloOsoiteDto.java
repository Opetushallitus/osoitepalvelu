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

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 5:32 PM
 */
public class HenkiloOsoiteDto implements Serializable {
    private static final long serialVersionUID  = -1L;

    private static final String POSTIOSOITE = "posti";
    private static final String DEFAULT_LANGUAGE = "fi";

    private Long id;
    private String kieli=DEFAULT_LANGUAGE;
    private String osoiteTyyppi=POSTIOSOITE;
    private String osoite;
    private String postilokero;
    private String postinumero;
    private String postitoimipaikka;
    private String extraRivi;
    private String henkiloEmail;
    private String puhelinnumero;

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli = kieli;
    }

    public String getOsoiteTyyppi() {
        return osoiteTyyppi;
    }

    public void setOsoiteTyyppi(String osoiteTyyppi) {
        this.osoiteTyyppi = osoiteTyyppi;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostilokero() {
        return postilokero;
    }

    public void setPostilokero(String postilokero) {
        this.postilokero = postilokero;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public String getExtraRivi() {
        return extraRivi;
    }

    public void setExtraRivi(String extraRivi) {
        this.extraRivi = extraRivi;
    }

    public String getHenkiloEmail() {
        return henkiloEmail;
    }

    public void setHenkiloEmail(String henkiloEmail) {
        this.henkiloEmail = henkiloEmail;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HenkiloOsoiteDto that = (HenkiloOsoiteDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
