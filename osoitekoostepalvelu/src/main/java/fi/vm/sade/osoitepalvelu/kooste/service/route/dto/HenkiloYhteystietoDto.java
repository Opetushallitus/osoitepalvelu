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

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:16 PM
 */
public class HenkiloYhteystietoDto implements Serializable {
    public static final String YHTESYTIETO_SAHKOPOSTI = "YHTEYSTIETO_SAHKOPOSTI";
    public static final String YHTEYSTIETO_PUHELINNUMERO = "YHTEYSTIETO_PUHELINNUMERO";
    public static final String YHTEYSTIETO_KATUOSOITE = "YHTEYSTIETO_KATUOSOITE";
    public static final String YHTEYSTIETO_POSTINUMERO = "YHTEYSTIETO_POSTINUMERO";
    public static final String YHTEYSTIETO_KUNTA = "YHTEYSTIETO_KUNTA";
    public static final String YHTEYSTIETO_KAUPUNKI = "YHTEYSTIETO_KAUPUNKI";

    private Long id;
    private String yhteystietoTyyppi;
    private String yhteystietoArvo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYhteystietoTyyppi() {
        return yhteystietoTyyppi;
    }

    public void setYhteystietoTyyppi(String yhteystietoTyyppi) {
        this.yhteystietoTyyppi = yhteystietoTyyppi;
    }

    public String getYhteystietoArvo() {
        return yhteystietoArvo;
    }

    public void setYhteystietoArvo(String yhteystietoArvo) {
        this.yhteystietoArvo = yhteystietoArvo;
    }
}
