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

package fi.vm.sade.osoitepalvelu.kooste.domain;

import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoElementtiDto;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:25 AM
 */
@Document(collection = "organisaatio")
public class OrganisaatioDetails implements Serializable {
    private static final long serialVersionUID = 442147524555663558L;
    
    private Long version;
    @Id
    @Indexed(unique = true)
    private String oid;
    @Indexed
    private String parentOid;
    @Indexed
    private DateTime cachedAt = new DateTime();
    private List<String> parentOidPath = new ArrayList<String>();
    private Map<String, String> nimi = new HashMap<String, String>();
    @Indexed
    private List<String> tyypit = new ArrayList<String>();
    @Indexed
    private List<String> kieletUris = new ArrayList<String>(); // opetuskieliUis, esim. oppilaitoksenopetuskieli_1#1
    @Indexed
    private String oppilaitosTyyppiUri; // esim. oppilaitostyyppi_21#1
    private String oppilaitosKoodi; // esim. 10107
    private String toimipistekoodi;
    @Indexed
    private String kotipaikkaUri; // esim. kunta_405
    private String maaUri; // esim. maatjavaltiot1_fin
    private OrganisaatioOsoiteDto postiosoite;
    private List<OrganisaatioDetailsYhteystietoDto> yhteystiedot
            = new ArrayList<OrganisaatioDetailsYhteystietoDto>();
    @Indexed
    private List<String> vuosiluokat = new ArrayList<String>();
    private List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos
            = new ArrayList<OrganisaatioYhteystietoElementtiDto>();

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public DateTime getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(DateTime cachedAt) {
        this.cachedAt = cachedAt;
    }

    public List<String> getParentOidPath() {
        return parentOidPath;
    }

    public void setParentOidPath(List<String> parentOidPath) {
        this.parentOidPath = parentOidPath;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public List<String> getTyypit() {
        return tyypit;
    }

    public void setTyypit(List<String> tyypit) {
        this.tyypit = tyypit;
    }

    public List<String> getKieletUris() {
        return kieletUris;
    }

    public void setKieletUris(List<String> kieletUris) {
        this.kieletUris = kieletUris;
    }

    public String getOppilaitosTyyppiUri() {
        return oppilaitosTyyppiUri;
    }

    public void setOppilaitosTyyppiUri(String oppilaitosTyyppiUri) {
        this.oppilaitosTyyppiUri = oppilaitosTyyppiUri;
    }

    public String getOppilaitosKoodi() {
        return oppilaitosKoodi;
    }

    public void setOppilaitosKoodi(String oppilaitosKoodi) {
        this.oppilaitosKoodi = oppilaitosKoodi;
    }

    public String getToimipistekoodi() {
        return toimipistekoodi;
    }

    public void setToimipistekoodi(String toimipistekoodi) {
        this.toimipistekoodi = toimipistekoodi;
    }

    public String getKotipaikkaUri() {
        return kotipaikkaUri;
    }

    public void setKotipaikkaUri(String kotipaikkaUri) {
        this.kotipaikkaUri = kotipaikkaUri;
    }

    public String getMaaUri() {
        return maaUri;
    }

    public void setMaaUri(String maaUri) {
        this.maaUri = maaUri;
    }

    public OrganisaatioOsoiteDto getPostiosoite() {
        return postiosoite;
    }

    public void setPostiosoite(OrganisaatioOsoiteDto postiosoite) {
        this.postiosoite = postiosoite;
    }

    public List<OrganisaatioDetailsYhteystietoDto> getYhteystiedot() {
        return yhteystiedot;
    }

    public void setYhteystiedot(List<OrganisaatioDetailsYhteystietoDto> yhteystiedot) {
        this.yhteystiedot = yhteystiedot;
    }

    public List<String> getVuosiluokat() {
        return vuosiluokat;
    }

    public void setVuosiluokat(List<String> vuosiluokat) {
        this.vuosiluokat = vuosiluokat;
    }

    public List<OrganisaatioYhteystietoElementtiDto> getYhteystietoArvos() {
        return yhteystietoArvos;
    }

    public void setYhteystietoArvos(List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos) {
        this.yhteystietoArvos = yhteystietoArvos;
    }
}
