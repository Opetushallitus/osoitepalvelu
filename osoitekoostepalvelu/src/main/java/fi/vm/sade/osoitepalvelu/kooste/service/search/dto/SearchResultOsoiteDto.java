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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.ratamaa.dtoconverter.annotation.DtoSkipped;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 4/1/14
 * Time: 12:39 PM
 */
@DtoConversion
public class SearchResultOsoiteDto implements Serializable {
    private static final long serialVersionUID = -870758978839602041L;

    private String kieli;
    @DtoPath("osoiteTyyppi")
    private String tyyppi;
    @DtoConversion(skip = true, withClass = HenkiloOsoiteDto.class)
    private String yhteystietoOid;
    private String osoite;
    private String postilokero;
    private String postinumero;
    private String postitoimipaikka;
    private String extraRivi;

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli = kieli;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public String getYhteystietoOid() {
        return yhteystietoOid;
    }

    public void setYhteystietoOid(String yhteystietoOid) {
        this.yhteystietoOid = yhteystietoOid;
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

    public EqualsHelper uniqueState() {
        return new EqualsHelper(
                this.yhteystietoOid,
                this.kieli,
                this.tyyppi,
                this.osoite,
                this.postinumero,
                this.postilokero,
                this.postitoimipaikka,
                this.extraRivi
        );
    }
}
