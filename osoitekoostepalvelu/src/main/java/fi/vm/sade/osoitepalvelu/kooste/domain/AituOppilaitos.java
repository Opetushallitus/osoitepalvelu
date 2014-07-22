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

import com.google.common.base.Function;

import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituSopimusDto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Document(collection  =  "aituOppilaitos")
public class AituOppilaitos {
    @Id
    private String oid;
    @Indexed
    private String oppilaitoskoodi;
    private Map<String,String> nimi = new HashMap<String, String>();
    private String osoite;
    private String postinumero;
    private String postitoimipaikka;
    private List<AituSopimusDto> sopimukset = new ArrayList<AituSopimusDto>();

    public final static Function<AituOppilaitos, List<AituSopimusDto>> SOPIMUKSET = new Function<AituOppilaitos, List<AituSopimusDto>>() {
        public List<AituSopimusDto> apply(@NotNull AituOppilaitos input) {
            return input.getSopimukset();
        }
    };

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOppilaitoskoodi() {
        return oppilaitoskoodi;
    }

    public void setOppilaitoskoodi(String oppilaitoskoodi) {
        this.oppilaitoskoodi = oppilaitoskoodi;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public List<AituSopimusDto> getSopimukset() {
        return sopimukset;
    }

    public void setSopimukset(List<AituSopimusDto> sopimukset) {
        this.sopimukset = sopimukset;
    }
}
