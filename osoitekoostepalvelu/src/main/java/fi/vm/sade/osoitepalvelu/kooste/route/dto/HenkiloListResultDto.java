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

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 1:10 PM
 */
public class HenkiloListResultDto implements Serializable {
    private static final long serialVersionUID = 4343659088794094264L;
    
    private String etunimet;
    private String kutsumanimi;
    private String sukunimi;
    private String oidHenkilo;
    private String kasittelijaOid;
    private String oppijanumero;
    private boolean passivoitu;
    private boolean duplicate;
    private HenkiloKayttajatiedotDto kayttajatiedot;

    public String getEtunimet() {
        return etunimet;
    }

    public void setEtunimet(String etunimet) {
        this.etunimet = etunimet;
    }

    public String getKutsumanimi() {
        return kutsumanimi;
    }
    
    public void setKutsumanimi(String kutsumanimi) {
        this.kutsumanimi = kutsumanimi;
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

    public HenkiloKayttajatiedotDto getKayttajatiedot() {
        return kayttajatiedot;
    }

    public void setKayttajatiedot(HenkiloKayttajatiedotDto kayttajatiedot) {
        this.kayttajatiedot = kayttajatiedot;
    }
}
