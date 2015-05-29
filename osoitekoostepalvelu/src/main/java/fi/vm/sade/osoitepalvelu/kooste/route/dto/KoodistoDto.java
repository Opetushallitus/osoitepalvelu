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
import java.util.HashMap;
import java.util.Map;

public class KoodistoDto implements Serializable {
    private static final long serialVersionUID = -4799209530323857832L;

    // Eri koodistojen tuetut kategoriat/tyypit
    public enum KoodistoTyyppi {
        //
        // Muista päivittää uriToTypeMapper -cache, jos lisäät arvoja tähän
        // enumiin
        OPPILAITOSTYYPPI("oppilaitostyyppi"),
        OMISTAJATYYPPI("omistajatyyppi"),
        VUOSILUOKAT("vuosiluokat"),
        MAAKUNTA("maakunta"),
        KUNTA("kunta"),
        POSTINUMERO("posti"),
        TUTKINTOTYYPPI("tutkintotyyppi"),
        KOULUTUSTYYPPI("koulutustyyppi"),
        KOULUTUSLAJI("koulutuslaji"),
        TUTKINTO("tutkinto"),
        KOULUTUS("koulutus"), // nämä käytössä AITU:ssa
        OPPILAITOKSEN_OPETUSKIELI("oppilaitoksenopetuskieli"),
        KIELI("kieli"),
        KOULUTUS_KIELIVALIKOIMA("kielivalikoima"),  // Koulutuksen kieli
        KOULUTUSALAOPH2002("koulutusalaoph2002"),      // Koulutusala
        KOULUTUSASTEKELA("koulutusastekela"),       // Koulutusaste
        KOULUTUSTOIMIJA("koulutustoimija"),         // Koulutuksen järjestäjä
        OPINTOALAOPH2002("opintoalaoph2002"),       // Koulutus ja Opintoala, nämä käytössä AITU:ssa
        ALUEHALLINTOVIRASTO("aluehallintovirasto"), // AluehallintoVIrasto (AVI)
        KAYTTOOIKEUSRYHMA("kayttoikeusryhma"),      // Ei Koodistosta, vaan auhtneitcation-servicestä
        TUTKINTOTOIMIKUNTA_ROOLIS("tutkintoimikuntarooli"), // Ei Koodistosta, vielä
        TUTKINTOTOIMIKUNTA("tutkintotoimikunta"); // Ei Koodistosta, vielä

        private String uri; // Koodiston URI

        // Cache, jolla merkkijonot voidaan nopeasti mapata KoodistoTyypeiksi
        private static Map<String, KoodistoTyyppi> uriToTypeMapper  =  new HashMap<String, KoodistoTyyppi>() {
            private static final long serialVersionUID  =  -4218132478973020911L;
            {
                put(OPPILAITOSTYYPPI.getUri(), OPPILAITOSTYYPPI);
                put(OMISTAJATYYPPI.getUri(), OMISTAJATYYPPI);
                put(VUOSILUOKAT.getUri(), VUOSILUOKAT);
                put(MAAKUNTA.getUri(), MAAKUNTA);
                put(KUNTA.getUri(), KUNTA);
                put(POSTINUMERO.getUri(), POSTINUMERO);
                put(TUTKINTOTYYPPI.getUri(), TUTKINTOTYYPPI);
                put(KOULUTUSTYYPPI.getUri(), KOULUTUSTYYPPI);
                put(KOULUTUSLAJI.getUri(), KOULUTUSLAJI);
                put(TUTKINTO.getUri(), TUTKINTO);
                put(KOULUTUS.getUri(), KOULUTUS);
                put(OPPILAITOKSEN_OPETUSKIELI.getUri(), OPPILAITOKSEN_OPETUSKIELI);
                put(KIELI.getUri(), KIELI);
                put(KOULUTUS_KIELIVALIKOIMA.getUri(), KOULUTUS_KIELIVALIKOIMA);
                put(KOULUTUSASTEKELA.getUri(), KOULUTUSASTEKELA);
                put(KOULUTUSALAOPH2002.getUri(), KOULUTUSALAOPH2002);
                put(KOULUTUSTOIMIJA.getUri(), KOULUTUSTOIMIJA);
                put(OPINTOALAOPH2002.getUri(), OPINTOALAOPH2002);
                put(ALUEHALLINTOVIRASTO.getUri(), ALUEHALLINTOVIRASTO);
            }
        };

        private KoodistoTyyppi(String uri) {
            this.uri  =  uri;
        }

        public String getUri() {
            return uri;
        }

        public static KoodistoTyyppi parseTyyppi(String koodistoTyyppi) {
            return uriToTypeMapper.get(koodistoTyyppi);
        }

        @Override
        public String toString() {
            return this.getUri();
        }
    }

    private KoodistoTyyppi tyyppi;
    private OrganisaatioOid organisaatioOid;
    private String uri;

    public KoodistoDto() {
    }

    public KoodistoDto(String koodistoUri, String organisaatioOid) {
        tyyppi  =  KoodistoTyyppi.parseTyyppi(koodistoUri);
        this.uri = koodistoUri;
        this.organisaatioOid  = new OrganisaatioOid(organisaatioOid);
    }

    public KoodistoTyyppi getTyyppi() {
        return this.tyyppi;
    }

    public String getKoodistoUri() {
        if (tyyppi != null) {
            return tyyppi.getUri();
        }
        return this.uri;
    }

    public void setKoodistoUri(String koodistoUri) {
        tyyppi  =  KoodistoTyyppi.parseTyyppi(koodistoUri);
        this.uri = koodistoUri;
    }

    public OrganisaatioOid getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(OrganisaatioOid organisaatioOid) {
        this.organisaatioOid  =  organisaatioOid;
    }

    @Override
    public String toString() {
        return (tyyppi != null ? tyyppi.name() : tyyppi) +  ", "  +  organisaatioOid;
    }
}
