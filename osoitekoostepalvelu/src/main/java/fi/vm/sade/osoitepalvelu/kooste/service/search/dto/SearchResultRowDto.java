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
import fi.ratamaa.dtoconverter.annotation.DtoConversions;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:34 PM
 */
@DtoConversions({
        @DtoConversion(path =  {"organisaatio", "henkilo", "osoite" }, withClass  =  OrganisaatioResultAggregateDto.class),
        @DtoConversion(path =  {"henkilo", "organisaatioHenkilo", "osoite"}, withClass = HenkiloResultAggregateDto.class)
})
public class SearchResultRowDto implements Serializable {
    private static final long serialVersionUID  =  -1252066099444560569L;

    @DtoConversion(path = "organisaatio.oid", withClass  =  OrganisaatioResultAggregateDto.class)
    private String organisaatioOid;
    private String kotikunta;
    private String toimipistekoodi;
    private String oppilaitosKoodi;
    private String wwwOsoite;
    @DtoConversion(skip = true)
    private String nimi;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String faksinumero;
    private String puhelinnumero;
    private String emailOsoite;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private List<String> tyypit;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String viranomaistiedotuksenEmail;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String koulutusneuvonnanEmail;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String kriisitiedotuksenEmail;

    @DtoConversion(path="organisaatioHenkilo.tehtavanimike", withClass = HenkiloResultAggregateDto.class)
    private String nimike;
    @DtoConversions({
            @DtoConversion(path = "henkilo.nimi", withClass  =  OrganisaatioResultAggregateDto.class),
            @DtoConversion(path = "henkilo.nimi", withClass = HenkiloResultAggregateDto.class)
    })
    private String yhteystietoNimi;
    @DtoConversion(path = "henkilo.email", withClass  =  OrganisaatioResultAggregateDto.class)
    private String henkiloEmail;
    @DtoConversion(path = "henkilo.yhteyshenkiloOid", withClass  =  OrganisaatioResultAggregateDto.class)
    private String henkiloOid;

    @DtoConversion(path = "osoite.kieli", withClass  =  OrganisaatioResultAggregateDto.class)
    private String osoiteKieli;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String osoiteTyyppi;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String yhteystietoOid;
    @DtoConversions({
        @DtoConversion(path = "osoite.osoite", withClass  =  OrganisaatioResultAggregateDto.class),
        @DtoConversion(path = "osoite.osoite", withClass  =  HenkiloResultAggregateDto.class)
    })
    private String osoite;
    @DtoConversion(skip = true, withClass = HenkiloResultAggregateDto.class)
    private String postilokero;
    private String postinumero;
    private String postitoimipaikka;
    private String extraRivi;


    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid  =  organisaatioOid;
    }

    public String getKotikunta() {
        return kotikunta;
    }

    public void setKotikunta(String kotikunta) {
        this.kotikunta  =  kotikunta;
    }

    public String getToimipistekoodi() {
        return toimipistekoodi;
    }

    public void setToimipistekoodi(String toimipistekoodi) {
        this.toimipistekoodi  =  toimipistekoodi;
    }

    public String getWwwOsoite() {
        return wwwOsoite;
    }

    public void setWwwOsoite(String wwwOsoite) {
        this.wwwOsoite  =  wwwOsoite;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi  =  nimi;
    }

    public String getFaksinumero() {
        return faksinumero;
    }

    public void setFaksinumero(String faksinumero) {
        this.faksinumero  =  faksinumero;
    }

    public String getEmailOsoite() {
        return emailOsoite;
    }

    public void setEmailOsoite(String emailOsoite) {
        this.emailOsoite  =  emailOsoite;
    }

    public String getNimike() {
        return nimike;
    }

    public void setNimike(String nimike) {
        this.nimike  =  nimike;
    }

    public String getYhteystietoNimi() {
        return yhteystietoNimi;
    }

    public void setYhteystietoNimi(String yhteystietoNimi) {
        this.yhteystietoNimi  =  yhteystietoNimi;
    }

    public String getHenkiloEmail() {
        return henkiloEmail;
    }

    public void setHenkiloEmail(String henkiloEmail) {
        this.henkiloEmail  =  henkiloEmail;
    }

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

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero  =  puhelinnumero;
    }

    public List<String> getTyypit() {
        return tyypit;
    }

    public void setTyypit(List<String> tyypit) {
        this.tyypit  =  tyypit;
    }

    public String getOsoiteKieli() {
        return osoiteKieli;
    }

    public void setOsoiteKieli(String osoiteKieli) {
        this.osoiteKieli  =  osoiteKieli;
    }

    public String getViranomaistiedotuksenEmail() {
        return viranomaistiedotuksenEmail;
    }

    public void setViranomaistiedotuksenEmail(String viranomaistiedotuksenEmail) {
        this.viranomaistiedotuksenEmail  =  viranomaistiedotuksenEmail;
    }

    public String getKoulutusneuvonnanEmail() {
        return koulutusneuvonnanEmail;
    }

    public void setKoulutusneuvonnanEmail(String koulutusneuvonnanEmail) {
        this.koulutusneuvonnanEmail  =  koulutusneuvonnanEmail;
    }

    public String getKriisitiedotuksenEmail() {
        return kriisitiedotuksenEmail;
    }

    public void setKriisitiedotuksenEmail(String kriisitiedotuksenEmail) {
        this.kriisitiedotuksenEmail  =  kriisitiedotuksenEmail;
    }

    public String getPostilokero() {
        return this.postilokero;
    }

    public void setPostilokero(String postilokero) {
        this.postilokero  =  postilokero;
    }

    public String getHenkiloOid() {
        return henkiloOid;
    }

    public void setHenkiloOid(String henkiloOid) {
        this.henkiloOid  =  henkiloOid;
    }

    public String getOppilaitosKoodi() {
        return oppilaitosKoodi;
    }

    public void setOppilaitosKoodi(String oppilaitosKoodi) {
        this.oppilaitosKoodi  =  oppilaitosKoodi;
    }

    @DtoConversion(skip = true)
    public OidAndTyyppiPair getOidAndTyyppiPair() {
        if(this.henkiloOid != null) {
            return new OidAndTyyppiPair(OidAndTyyppiPair.TYYPPI_HENKILO, this.henkiloOid);
        }
        return new OidAndTyyppiPair(OidAndTyyppiPair.TYYPPI_ORGANISAATIO, this.organisaatioOid);
    }
}
