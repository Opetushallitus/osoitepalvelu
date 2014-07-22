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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoPath;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioviiteDto;

public class UiKoodiItemDto implements Serializable {
    private static final long serialVersionUID = 303815492103134497L;
    
    @DtoConversion
    @DtoPath(value = "koodisto.tyyppi", withClass  =  KoodiDto.class)
    private KoodistoTyyppi koodistonTyyppi; // Kategoria
    @DtoPath(value = "koodiArvo", withClass  =  KoodiDto.class)
    private String koodiId; // koodiArvo koodistossa
    private String koodiUri;
    private String nimi;
    private String kuvaus;
    private String lyhytNimi;
    @DtoConversion(withClass = KoodiDto.class)
    private List<OrganisaatioviiteDto> organisaatioViite  =  new ArrayList<OrganisaatioviiteDto>();

    public static final Function<UiKoodiItemDto, String> URI = new Function<UiKoodiItemDto, String>() {
        public String apply(UiKoodiItemDto item) {
            return item.getKoodiUri(); // NOSONAR (http://sourceforge.net/p/findbugs/bugs/1139/)
        }
    };
    public static final Function<UiKoodiItemDto, String> KOODI_ID = new Function<UiKoodiItemDto, String>() {
        public String apply(UiKoodiItemDto item) {
            return item.getKoodiId(); // NOSONAR
        }
    };

    public KoodistoTyyppi getKoodistonTyyppi() {
        return koodistonTyyppi;
    }

    public void setKoodistonTyyppi(KoodistoTyyppi koodistonTyyppi) {
        this.koodistonTyyppi  =  koodistonTyyppi;
    }

    public String getKoodiId() {
        return koodiId;
    }

    public void setKoodiId(String koodiId) {
        this.koodiId  =  koodiId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi  =  nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus  =  kuvaus;
    }

    public String getLyhytNimi() {
        return lyhytNimi;
    }

    public void setLyhytNimi(String lyhytNimi) {
        this.lyhytNimi  =  lyhytNimi;
    }

    public String toString() {
        return this.koodiId  +  ": "  +  this.nimi;
    }

    public List<OrganisaatioviiteDto> getOrganisaatioViite() {
        return organisaatioViite;
    }

    public void setOrganisaatioViite(List<OrganisaatioviiteDto> organisaatioViite) {
        this.organisaatioViite  =  organisaatioViite;
    }

    public String getKoodiUri() {
        return koodiUri;
    }

    public void setKoodiUri(String koodiUri) {
        this.koodiUri  =  koodiUri;
    }
}
