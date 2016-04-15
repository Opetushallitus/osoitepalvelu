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

import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:06 PM
 */
public class OrganisaatioYhteystietoHakuResultDto implements Serializable, FilterableOrganisaatio {
    private static final long serialVersionUID = 2642158081723050787L;
    
    private String oid;
    private Map<String, String> nimi  =  new HashMap<String, String>(); // short lowercase lang code  = > value pairs
    private List<String> tyypit  =  new ArrayList<String>();
    private List<String> kielet  =  new ArrayList<String>(); // koodiarvot
    private String kotipaikka;
    private String oppilaitosKoodi;
    private String toimipisteKoodi;
    private List<OrganisaatioYhteysosoiteDto> postiosoite  =  new ArrayList<OrganisaatioYhteysosoiteDto>();
    private List<OrganisaatioYhteysosoiteDto> kayntiosoite  =  new ArrayList<OrganisaatioYhteysosoiteDto>();
    private String oppilaitosTyyppiUri; // esim. oppilaitostyyppi_21#1
    private List<OrganisaatioYhteystietoElementtiDto> kriisitiedotuksenEmail = new ArrayList<OrganisaatioYhteystietoElementtiDto>();

    @Override
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid  =  oid;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi  =  nimi;
    }

    @Override
    public List<String> getTyypit() {
        return tyypit;
    }

    public void setTyypit(List<String> tyypit) {
        this.tyypit  =  tyypit;
    }

    @Override
    public List<String> getKielet() {
        return kielet;
    }

    public void setKielet(List<String> kielet) {
        this.kielet  =  kielet;
    }

    @Override
    public String getKotipaikka() {
        return kotipaikka;
    }

    public void setKotipaikka(String kotipaikka) {
        this.kotipaikka  =  kotipaikka;
    }

    public String getOppilaitosKoodi() {
        return oppilaitosKoodi;
    }

    public void setOppilaitosKoodi(String oppilaitosKoodi) {
        this.oppilaitosKoodi  =  oppilaitosKoodi;
    }

    public List<OrganisaatioYhteysosoiteDto> getPostiosoite() {
        return postiosoite;
    }

    public void setPostiosoite(List<OrganisaatioYhteysosoiteDto> postiosoite) {
        this.postiosoite  =  postiosoite;
    }

    public List<OrganisaatioYhteysosoiteDto> getKayntiosoite() {
        return kayntiosoite;
    }

    public void setKayntiosoite(List<OrganisaatioYhteysosoiteDto> kayntiosoite) {
        this.kayntiosoite  =  kayntiosoite;
    }

    public String getToimipisteKoodi() {
        return toimipisteKoodi;
    }

    public void setToimipisteKoodi(String toimipisteKoodi) {
        this.toimipisteKoodi  =  toimipisteKoodi;
    }

    public String getOppilaitosTyyppiUri() {
        return oppilaitosTyyppiUri;
    }

    public void setOppilaitosTyyppiUri(String oppilaitosTyyppiUri) {
        this.oppilaitosTyyppiUri = oppilaitosTyyppiUri;
    }

    public List<OrganisaatioYhteystietoElementtiDto> getKriisitiedotuksenEmail() {
        return kriisitiedotuksenEmail;
    }

    public void setKriisitiedotuksenEmail(List<OrganisaatioYhteystietoElementtiDto> kriisitiedotuksenEmail) {
        this.kriisitiedotuksenEmail = kriisitiedotuksenEmail;
    }
}
