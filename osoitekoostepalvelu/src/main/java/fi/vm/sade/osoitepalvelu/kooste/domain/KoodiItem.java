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

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/17/13
 * Time: 9:05 AM
 */
public class KoodiItem implements Serializable {
    private static final long serialVersionUID = -6732834870766631012L;
    
    private KoodistoCache.KoodistoTyyppi koodistonTyyppi;
    private String koodiId;
    private String koodiUri;
    private String nimi;
    private String kuvaus;
    private String lyhytNimi;
    private List<Organisaatioviite> organisaatioViite;

    public String getKoodiId() {
        return koodiId;
    }

    public void setKoodiId(String koodiId) {
        this.koodiId = koodiId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getLyhytNimi() {
        return lyhytNimi;
    }

    public void setLyhytNimi(String lyhytNimi) {
        this.lyhytNimi = lyhytNimi;
    }

    public KoodistoCache.KoodistoTyyppi getKoodistonTyyppi() {
        return koodistonTyyppi;
    }

    public void setKoodistonTyyppi(KoodistoCache.KoodistoTyyppi koodistonTyyppi) {
        this.koodistonTyyppi = koodistonTyyppi;
    }

    public List<Organisaatioviite> getOrganisaatioViite() {
        return organisaatioViite;
    }

    public void setOrganisaatioViite(List<Organisaatioviite> organisaatioViite) {
        this.organisaatioViite = organisaatioViite;
    }

    public String getKoodiUri() {
        return koodiUri;
    }

    public void setKoodiUri(String koodiUri) {
        this.koodiUri = koodiUri;
    }
}