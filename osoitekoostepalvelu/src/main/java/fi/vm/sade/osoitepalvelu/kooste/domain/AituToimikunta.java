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

import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituJasenyysDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Document(collection = "aituToimikunta")
public class AituToimikunta {
    @Id
    private String id; // id in AITU
    private Map<String,String> nimi = new HashMap<String, String>();
    private String sahkoposti;
    private String kielisyys; // short lower case language code, e.g. "fi", but may be also 2k (kaksikielinen = fi/sv)
    private List<AituJasenyysDto> jasenyydet = new ArrayList<AituJasenyysDto>();
    private String toimikausi; // free text

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getKielisyys() {
        return kielisyys;
    }

    public void setKielisyys(String kielisyys) {
        this.kielisyys = kielisyys;
    }

    public List<AituJasenyysDto> getJasenyydet() {
        return jasenyydet;
    }

    public void setJasenyydet(List<AituJasenyysDto> jasenyydet) {
        this.jasenyydet = jasenyydet;
    }

    public String getToimikausi() {
        return toimikausi;
    }

    public void setToimikausi(String toimikausi) {
        this.toimikausi = toimikausi;
    }
}