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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
public class OrganisaatioDetails implements Serializable, FilterableOrganisaatio {
    private static final long serialVersionUID = 442147524555663558L;
    
    private Long version;
    @Id
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
    @Indexed
    private String oppilaitosKoodi; // esim. 10107
    @DtoConversion(path="toimipisteKoodi", withClass = OrganisaatioYhteystietoHakuResultDto.class)
    private String toimipistekoodi;
    @Indexed
    @DtoConversion(path="kotipaikka", withClass = OrganisaatioYhteystietoHakuResultDto.class)
    private String kotipaikkaUri; // esim. kunta_405
    private String maaUri; // esim. maatjavaltiot1_fin
    private OrganisaatioOsoiteDto postiosoite;
    private OrganisaatioOsoiteDto kayntiosoite;
    private List<OrganisaatioDetailsYhteystietoDto> yhteystiedot
            = new ArrayList<OrganisaatioDetailsYhteystietoDto>();
    @Indexed
    private List<String> vuosiluokat = new ArrayList<String>();

    private String kriisitiedotuksenEmail;

    private String varhaiskasvatuksenYhteyshenkilo;
    private String varhaiskasvatuksenEmail;
    private List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos
            = new ArrayList<OrganisaatioYhteystietoElementtiDto>();
    @Indexed
    private String ytunnus;
    private LocalDate alkuPvm;
    @Indexed
    private LocalDate lakkautusPvm;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
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

    @Override
    public List<String> getTyypit() {
        return tyypit;
    }

    @Override
    public List<String> getKielet() {
        return getKieletUris();
    }

    @Override
    public String getKotipaikka() {
        return getKotipaikkaUri();
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

    public String getKriisitiedotuksenEmail() {
        return kriisitiedotuksenEmail;
    }

    public void setKriisitiedotuksenEmail(String kriisitiedotuksenEmail) {
        this.kriisitiedotuksenEmail = kriisitiedotuksenEmail;
    }

    public String getVarhaiskasvatuksenYhteyshenkilo() { return varhaiskasvatuksenYhteyshenkilo; }

    public void setVarhaiskasvatuksenYhteyshenkilo(String varhaiskasvatuksenYhteyshenkilo) {
        this.varhaiskasvatuksenYhteyshenkilo = varhaiskasvatuksenYhteyshenkilo;
    }

    public String getVarhaiskasvatuksenEmail() { return varhaiskasvatuksenEmail; }

    public void setVarhaiskasvatuksenEmail(String varhaiskasvatuksenEmail) {
        this.varhaiskasvatuksenEmail = varhaiskasvatuksenEmail;
    }

    public List<OrganisaatioYhteystietoElementtiDto> getYhteystietoArvos() {
        return yhteystietoArvos;
    }

    public void setYhteystietoArvos(List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos) {
        this.yhteystietoArvos = yhteystietoArvos;
    }

    public OrganisaatioOsoiteDto getKayntiosoite() {
        return kayntiosoite;
    }

    public void setKayntiosoite(OrganisaatioOsoiteDto kayntiosoite) {
        this.kayntiosoite = kayntiosoite;
    }

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public LocalDate getLakkautusPvm() {
        return lakkautusPvm;
    }

    public void setLakkautusPvm(LocalDate lakkautusPvm) {
        this.lakkautusPvm = lakkautusPvm;
    }

    @Transient
    public boolean isLakkautettu() {
        return this.lakkautusPvm != null && this.lakkautusPvm.compareTo(new LocalDate()) <= 0;
    }
}
