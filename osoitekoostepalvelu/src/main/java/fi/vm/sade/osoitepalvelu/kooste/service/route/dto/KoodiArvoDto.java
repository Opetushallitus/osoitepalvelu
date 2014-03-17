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

package fi.vm.sade.osoitepalvelu.kooste.service.route.dto;

import java.io.Serializable;

public class KoodiArvoDto implements Serializable {
    private String nimi;
    private String kuvaus;
    private String lyhytNimi;
    private String kieli;

    public KoodiArvoDto() {
    }

    public KoodiArvoDto(String nimi, String kuvaus, String lyhytNimi, String kieli) {
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.lyhytNimi = lyhytNimi;
        this.kieli = kieli;
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

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli = kieli;
    }

    @Override
    public String toString() {
        return this.nimi;
    }
}
