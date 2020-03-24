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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.validation.constraints.NotNull;
import java.util.Set;

import java.io.Serializable;
import java.util.*;

public class OrganisaatioDetails implements Serializable, FilterableOrganisaatio {
    private static final long serialVersionUID = 442147524555663558L;

    /**
     * Aikaleima jolloin organisaatiotietojen malli on viimeksi muuttunut.
     * Jos organisaatiotiedot muuttuvat (tähän luokkaan esim. lisätään uusi kenttä),
     * muuta tämä aikaleima nykyhetkeen jotta mongossa oleva välimuisti päivittyy
     * asennuksen yhteydessä (muuten välimuisti päivittyy vasta yöllä).
     */
    public static final DateTime MODEL_CHANGED_AT = new DateTime(2019, 5, 9, 7, 35);

    private Long version;
    private String oid;
    //index
    private String parentOid;
    //index
    private DateTime cachedAt;
    private Set<String> parentOidPath;

    private Map<String, String> nimi;

    private Set<String> tyypit;

    private Set<String> kieletUris; // opetuskieliUis, esim. oppilaitoksenopetuskieli_1#1

    //index
    private String oppilaitosTyyppiUri; // esim. oppilaitostyyppi_21#1

    //index
    private String oppilaitosKoodi; // esim. 10107

    //@DtoConversion(path="toimipisteKoodi", withClass = OrganisaatioYhteystietoHakuResultDto.class)
    private String toimipistekoodi;

    //@DtoConversion(path="kotipaikka", withClass = OrganisaatioYhteystietoHakuResultDto.class)
    //@Column //index
    private String kotipaikkaUri; // esim. kunta_405

    private String maaUri; // esim. maatjavaltiot1_fin

    private OrganisaatioOsoiteDto postiosoite;

    private List<OrganisaatioDetailsYhteystietoDto> yhteystiedot
            = new ArrayList<OrganisaatioDetailsYhteystietoDto>();
    private List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos
            = new ArrayList<OrganisaatioYhteystietoElementtiDto>();
    // index
    private Set<String> vuosiluokat;

    private String kriisitiedotuksenEmail;

    private String varhaiskasvatuksenYhteyshenkilo;
    private String varhaiskasvatuksenEmail;
    // index
    private String ytunnus;
    private String yritysmuoto;
    private LocalDate alkuPvm;
    // index
    private LocalDate lakkautusPvm;
    private String koskiYhdyshenkilo;

    // index
    private Set<String> koulutusluvat; // koodisto "koulutus" (oiva)

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

    public Set<String> getParentOidPath() {
        return parentOidPath;
    }

    public void setParentOidPath(Set<String> parentOidPath) {
        this.parentOidPath = parentOidPath;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    @Override
    public Set<String> getTyypit() {
        return tyypit;
    }

    @Override
    public Set<String> getKielet() {
        return getKieletUris();
    }

    @Override
    public String getKotipaikka() {
        return getKotipaikkaUri();
    }

    public void setTyypit(Set<String> tyypit) {
        this.tyypit = tyypit;
    }

    public Set<String> getKieletUris() {
        return kieletUris;
    }

    public void setKieletUris(Set<String> kieletUris) {
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

    public Set<String> getVuosiluokat() {
        return vuosiluokat;
    }

    public void setVuosiluokat(Set<String> vuosiluokat) {
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

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    public String getKoskiYhdyshenkilo() {
        return koskiYhdyshenkilo;
    }

    public void setKoskiYhdyshenkilo(String koskiYhdyshenkilo) {
        this.koskiYhdyshenkilo = koskiYhdyshenkilo;
    }

    public String getYritysmuoto() {
        return yritysmuoto;
    }

    public void setYritysmuoto(String yritysmuoto) {
        this.yritysmuoto = yritysmuoto;
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

    public Set<String> getKoulutusluvat() {
        return koulutusluvat;
    }

    public void setKoulutusluvat(Set<String> koulutusluvat) {
        this.koulutusluvat = koulutusluvat;
    }

    @Transient
    public boolean isLakkautettu() {
        return this.lakkautusPvm != null && this.lakkautusPvm.compareTo(new LocalDate()) <= 0;
    }
}
