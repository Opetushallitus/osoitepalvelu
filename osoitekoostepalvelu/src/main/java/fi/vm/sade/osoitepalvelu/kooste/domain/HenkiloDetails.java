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

import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloKieliDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloYhteystietoRyhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioHenkiloDto;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:34 PM
 */
@Document(collection = "henkilo")
public class HenkiloDetails {
    @Id
    @Indexed(unique = true)
    private String oidHenkilo;
    private String etunimet;
    private String kutsunanimi;
    private String sukunimi;
    private String kasittelijaOid;
    private String oppijanumero;
    private boolean passivoitu;
    private boolean duplicate;
    private DateTime cachedAt = new DateTime();
    @Indexed
    private Set<String> aktiivinenOrganisaatioOids = new HashSet<String>();
    private List<OrganisaatioHenkiloDto> organisaatiohenkilos = new ArrayList<OrganisaatioHenkiloDto>();
    private HenkiloKieliDto asiointiKieli;
    private List<HenkiloYhteystietoRyhmaDto> yhteystiedotRyhma = new ArrayList<HenkiloYhteystietoRyhmaDto>();

    public String getEtunimet() {
        return etunimet;
    }

    public void setEtunimet(String etunimet) {
        this.etunimet = etunimet;
    }

    public String getKutsunanimi() {
        return kutsunanimi;
    }

    public void setKutsunanimi(String kutsunanimi) {
        this.kutsunanimi = kutsunanimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getOidHenkilo() {
        return oidHenkilo;
    }

    public void setOidHenkilo(String oidHenkilo) {
        this.oidHenkilo = oidHenkilo;
    }

    public String getKasittelijaOid() {
        return kasittelijaOid;
    }

    public void setKasittelijaOid(String kasittelijaOid) {
        this.kasittelijaOid = kasittelijaOid;
    }

    public String getOppijanumero() {
        return oppijanumero;
    }

    public void setOppijanumero(String oppijanumero) {
        this.oppijanumero = oppijanumero;
    }

    public boolean isPassivoitu() {
        return passivoitu;
    }

    public void setPassivoitu(boolean passivoitu) {
        this.passivoitu = passivoitu;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public HenkiloKieliDto getAsiointiKieli() {
        return asiointiKieli;
    }

    public void setAsiointiKieli(HenkiloKieliDto asiointiKieli) {
        this.asiointiKieli = asiointiKieli;
    }

    public List<HenkiloYhteystietoRyhmaDto> getYhteystiedotRyhma() {
        return yhteystiedotRyhma;
    }

    public void setYhteystiedotRyhma(List<HenkiloYhteystietoRyhmaDto> yhteystiedotRyhma) {
        this.yhteystiedotRyhma = yhteystiedotRyhma;
    }

    public DateTime getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(DateTime cachedAt) {
        this.cachedAt = cachedAt;
    }

    public Set<String> getAktiivinenOrganisaatioOids() {
        return aktiivinenOrganisaatioOids;
    }

    public void setAktiivinenOrganisaatioOids(Set<String> aktiivinenOrganisaatioOids) {
        this.aktiivinenOrganisaatioOids = aktiivinenOrganisaatioOids;
    }

    public List<OrganisaatioHenkiloDto> getOrganisaatiohenkilos() {
        return organisaatiohenkilos;
    }

    public void setOrganisaatiohenkilos(List<OrganisaatioHenkiloDto> organisaatiohenkilos) {
        this.organisaatiohenkilos = organisaatiohenkilos;
    }
}
