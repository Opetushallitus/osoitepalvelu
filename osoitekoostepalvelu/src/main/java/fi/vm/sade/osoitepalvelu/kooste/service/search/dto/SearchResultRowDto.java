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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:34 PM
 */
@DtoConversion(path={"organisaatio", "henkilo", "osoite"}, withClass = ResultAggregateDto.class)
public class SearchResultRowDto implements Serializable {
    @DtoConversion(path="organisaatio.oid", withClass = ResultAggregateDto.class)
    private String organisaatioOid;
    private String kotikunta;
    private String toimipistekoodi;
    private String wwwOsoite;
    @DtoConversion(skip = true, withClass = ResultAggregateDto.class)
    private String nimi;
    private String faksinumero;
    private String puhelinnumero;
    private String emailOsoite;
    private List<String> tyypit;

    private Set<String> roolit;
    private String etunimi;
    private String sukunimi;
    private String email;
    @DtoConversion(path="henkilo.oid", withClass = ResultAggregateDto.class)
    private String henkiloOid;

    @DtoConversion(path="osoite.kieli", withClass = ResultAggregateDto.class)
    private String osoiteKieli;
    private String osoiteTyyppi;
    private String yhteystietoOid;
    @DtoConversion(path="osoite.osoite", withClass = ResultAggregateDto.class)
    private String osoite;
    private String postinumero;
    private String postitoimipaikka;
    private String extraRivi;


    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }

    public String getKotikunta() {
        return kotikunta;
    }

    public void setKotikunta(String kotikunta) {
        this.kotikunta = kotikunta;
    }

    public String getToimipistekoodi() {
        return toimipistekoodi;
    }

    public void setToimipistekoodi(String toimipistekoodi) {
        this.toimipistekoodi = toimipistekoodi;
    }

    public String getWwwOsoite() {
        return wwwOsoite;
    }

    public void setWwwOsoite(String wwwOsoite) {
        this.wwwOsoite = wwwOsoite;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getFaksinumero() {
        return faksinumero;
    }

    public void setFaksinumero(String faksinumero) {
        this.faksinumero = faksinumero;
    }

    public String getEmailOsoite() {
        return emailOsoite;
    }

    public void setEmailOsoite(String emailOsoite) {
        this.emailOsoite = emailOsoite;
    }

    public Set<String> getRoolit() {
        return roolit;
    }

    public void setRoolit(Set<String> roolit) {
        this.roolit = roolit;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHenkiloOid() {
        return henkiloOid;
    }

    public void setHenkiloOid(String henkiloOid) {
        this.henkiloOid = henkiloOid;
    }

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

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
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

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public List<String> getTyypit() {
        return tyypit;
    }

    public void setTyypit(List<String> tyypit) {
        this.tyypit = tyypit;
    }

    public String getOsoiteKieli() {
        return osoiteKieli;
    }

    public void setOsoiteKieli(String osoiteKieli) {
        this.osoiteKieli = osoiteKieli;
    }

    @DtoConversion(skip = true) /*TODO*/
    public String getPostilokero() {
        return getOsoite();
    }

    @DtoConversion(skip = true) /*TODO*/
    public String getVirnaomaistiedotuksenEmail() {
        return getEmailOsoite();
    }

    @DtoConversion(skip = true) /*TODO*/
    public String getKoulutusneuvonnanEmail() {
        return getEmailOsoite();
    }

    @DtoConversion(skip = true) /*TODO*/
    public String getKriisitiedotuksenEmail() {
        return getEmailOsoite();
    }
}
