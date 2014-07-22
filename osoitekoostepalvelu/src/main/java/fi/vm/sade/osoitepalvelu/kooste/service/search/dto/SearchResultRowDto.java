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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import fi.ratamaa.dtoconverter.annotation.*;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:34 PM
 */
@ApiModel("Kuvaa hakutulosriviä")
public class SearchResultRowDto implements Serializable {
    private static final long serialVersionUID  =  -1252066099444560569L;

    @ApiModelProperty("Hakutuloksen rivinumero")
    @DtoSkipped
    private int rivinumero = 0;
    @ApiModelProperty("Organisaation nimi")
    @DtoSkipped
    private String nimi;

    @DtoConversion(path = "organisaatio.oid", with="organisaatioAggregate")
    @DtoPath(value="organisaatioHenkilo.organisaatioOid", with="henkiloAggregate")
    private String organisaatioOid;
    @DtoConversion(path="organisaatio.kotikunta", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String kotikunta;
    @DtoConversion(path="organisaatio.toimipistekoodi", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String toimipistekoodi;
    @DtoConversion(path="organisaatio.oppilaitosKoodi", with="organisaatioAggregate")
    @DtoPath(value="oppilaitos.oppilaitoskoodi", with = "aituOppilaitosAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String oppilaitosKoodi;
    @DtoConversion(path="organisaatio.wwwOsoite", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String wwwOsoite;
    @DtoConversion(path="organisaatio.tyypit", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private List<String> tyypit;
    @DtoConversion(path="organisaatio.faksinumero", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String faksinumero;
    @DtoConversion(path="organisaatio.puhelinnumero", with="organisaatioAggregate")
    @DtoPath(value="osoite.puhelinnumero", with="henkiloAggregate")
    private String puhelinnumero;
    @DtoConversion(path="organisaatio.emailOsoite", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String emailOsoite;
    @DtoConversion(path="organisaatio.viranomaistiedotuksenEmail", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String viranomaistiedotuksenEmail;
    @DtoConversion(path="organisaatio.koulutusneuvonnanEmail", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String koulutusneuvonnanEmail;
    @DtoConversion(path="organisaatio.kriisitiedotuksenEmail", with="organisaatioAggregate")
    @DtoSkipped(with="henkiloAggregate")
    private String kriisitiedotuksenEmail;

    @DtoConversion(path = "henkilo.yhteyshenkiloOid", with="organisaatioAggregate")
    @DtoPath(value="henkilo.henkiloOid", with="henkiloAggregate")
    private String henkiloOid;
    @DtoConversions({
        @DtoConversion(path="henkilo.nimi", with="organisaatioAggregate"),
        @DtoConversion(path="henkilo.nimi", with="henkiloAggregate"),
        @DtoConversion(path="tutkinto.vastuuhenkilo", with = "aituOppilaitosAggregate")
    })
    private String yhteystietoNimi;
    @DtoConversion(path="henkilo.nimike", with="organisaatioAggregate")
    @DtoPath(value="organisaatioHenkilo.tehtavanimike", with="henkiloAggregate")
    private String nimike;
    @DtoConversion(path = "henkilo.email", with="organisaatioAggregate")
    @DtoPaths({
        @DtoPath(value = "osoite.henkiloEmail", with="henkiloAggregate"),
        @DtoPath(value="tutkinto.sahkoposti_vastuuhenkilo",  with = "aituOppilaitosAggregate")
    })
    private String henkiloEmail;
    @DtoConversion(path = "osoite", with="henkiloAggregate")
    @DtoPath(value = "kayntiosoite", with="organisaatioAggregate")
    private SearchResultOsoiteDto kayntiosoite;
    @DtoConversion(path = "osoite", with="henkiloAggregate")
    @DtoPaths({
        @DtoPath(value="oppilaitos", with = "aituOppilaitosAggregate"),
        @DtoPath(value = "postiosoite", with="organisaatioAggregate")
    })
    private SearchResultOsoiteDto postiosoite;

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

    public SearchResultOsoiteDto getKayntiosoite() {
        return kayntiosoite;
    }

    public void setKayntiosoite(SearchResultOsoiteDto kayntiosoite) {
        this.kayntiosoite = kayntiosoite;
    }

    public SearchResultOsoiteDto getPostiosoite() {
        return postiosoite;
    }

    public void setPostiosoite(SearchResultOsoiteDto postiosoite) {
        this.postiosoite = postiosoite;
    }

    public int getRivinumero() {
        return rivinumero;
    }

    public void setRivinumero(int rivinumero) {
        this.rivinumero = rivinumero;
    }

    @DtoConversion(skip = true)
    public OidAndTyyppiPair getOidAndTyyppiPair() {
        if(this.henkiloOid != null) {
            return new OidAndTyyppiPair(OidAndTyyppiPair.TYYPPI_HENKILO, this.henkiloOid, this.rivinumero);
        }
        return new OidAndTyyppiPair(OidAndTyyppiPair.TYYPPI_ORGANISAATIO, this.organisaatioOid, this.rivinumero);
    }

    public EqualsHelper uniqueState() {
        return new EqualsHelper(
                this.organisaatioOid,
                this.oppilaitosKoodi,
                this.emailOsoite,
                this.henkiloOid,
                this.henkiloEmail,
                this.yhteystietoNimi,
                this.nimi,
                this.nimike,
                this.tyypit,
                this.postiosoite != null ? this.postiosoite.uniqueState() : null,
                this.kayntiosoite != null ? this.kayntiosoite.uniqueState() : null,
                this.toimipistekoodi,
                this.puhelinnumero,
                this.kotikunta,
                this.wwwOsoite,
                this.koulutusneuvonnanEmail,
                this.kriisitiedotuksenEmail,
                this.viranomaistiedotuksenEmail,
                this.faksinumero
        );
    }
}
