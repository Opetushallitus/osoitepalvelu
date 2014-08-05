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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:16 PM
 */
public class OrganisaatioYhteysosoiteDto implements Serializable {
    private static final long serialVersionUID = 4643435353987836843L;

    /**
     * @see fi.vm.sade.organisaatio.api.model.types.OsoiteTyyppi
     */
    public static enum OsoiteTyyppi {
        posti,
        kaynti,
        ruotsi_posti,
        ruotsi_kaynti,
        ulkomainen_posti,
        ulkomainen_kaynti,
        muu;
    }

    private String kieli; // koodiarvo
    private OsoiteTyyppi osoiteTyyppi;
    private String osoite;
    @DtoConversion(withClass = OrganisaatioDetailsYhteystietoDto.class, path = "postinumeroUri")
    private String postinumero; // koodiarvo

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli  =  kieli;
    }

    public OsoiteTyyppi getOsoiteTyyppi() {
        return osoiteTyyppi;
    }

    public void setOsoiteTyyppi(OsoiteTyyppi osoiteTyyppi) {
        this.osoiteTyyppi  =  osoiteTyyppi;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite  =  osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero  =  postinumero;
    }
}
