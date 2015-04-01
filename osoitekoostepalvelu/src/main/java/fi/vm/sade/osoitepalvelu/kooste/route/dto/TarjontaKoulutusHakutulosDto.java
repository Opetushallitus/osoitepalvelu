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
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
public class TarjontaKoulutusHakutulosDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TarjontaTila {
        POISTETTU,
        LUONNOS,
        VALMIS,
        JULKAISTU,
        PERUTTU,
        KOPIOITU;
    }

    public enum KoulutusmoduuliTyyppi {

        TUTKINNON_OSA,
        TUTKINTO,
        TUTKINTO_OHJELMA;
    }

    public enum ToteutustyyppiEnum {

        AMMATILLINEN_PERUSTUTKINTO("koulutustyyppi_1"),
        LUKIOKOULUTUS("koulutustyyppi_2"),
        KORKEAKOULUTUS("koulutustyyppi_3"),
        AMMATILLINEN_PERUSKOULUTUS_ERITYISOPETUKSENA("koulutustyyppi_4"),
        VALMENTAVA_JA_KUNTOUTTAVA_OPETUS_JA_OHJAUS("koulutustyyppi_5"),
        PERUSOPETUKSEN_LISAOPETUS("koulutustyyppi_6"),
        KORKEAKOULUOPINTO("koulutustyyppi_3"),  // opintokokonaisuus or opintojakso
        AMMATILLISEEN_PERUSKOULUTUKSEEN_OHJAAVA_JA_VALMISTAVA_KOULUTUS("koulutustyyppi_7"),
        AMMATILLISEEN_PERUSKOULUTUKSEEN_VALMENTAVA("koulutustyyppi_18"),
        AMMATILLISEEN_PERUSKOULUTUKSEEN_VALMENTAVA_ER("koulutustyyppi_19"),
        MAAHANMUUTTAJIEN_AMMATILLISEEN_PERUSKOULUTUKSEEN_VALMISTAVA_KOULUTUS("koulutustyyppi_8"),
        MAAHANMUUTTAJIEN_JA_VIERASKIELISTEN_LUKIOKOULUTUKSEEN_VALMISTAVA_KOULUTUS("koulutustyyppi_9"),
        VAPAAN_SIVISTYSTYON_KOULUTUS("koulutustyyppi_10"),
        AMMATTITUTKINTO("koulutustyyppi_11"),
        ERIKOISAMMATTITUTKINTO("koulutustyyppi_12"),
        AMMATILLINEN_PERUSTUTKINTO_NAYTTOTUTKINTONA("koulutustyyppi_13"),
        LUKIOKOULUTUS_AIKUISTEN_OPPIMAARA("koulutustyyppi_14"),
        EB_RP_ISH("koulutustyyppi_20"),
        ESIOPETUS("koulutustyyppi_15"),
        PERUSOPETUS("koulutustyyppi_16"),
        AIKUISTEN_PERUSOPETUS("koulutustyyppi_17"),
        ERIKOISAMMATTITUTKINTO_VALMISTAVA(null),
        AMMATTITUTKINTO_VALMISTAVA(null),
        AMMATILLINEN_PERUSTUTKINTO_NAYTTOTUTKINTONA_VALMISTAVA(null);


        final private String koulutustyyppiUri;

        private ToteutustyyppiEnum(String koulutustyyppiUri) {
            this.koulutustyyppiUri = koulutustyyppiUri;
        }

        public String uri() {
            return this.koulutustyyppiUri;
        }

    }

    private String oid;
    private Map<String, String> nimi;
    private Map<String, String> kausi;
    private String kausiUri;
    private Integer vuosi;
    private Map<String, String> koulutusLaji;
    private String koulutuslajiUri;
    private TarjontaTila tila;

    /// @TODO: Tätä varmaan ei tarvita...
    private ToteutustyyppiEnum toteutustyyppiEnum;
    private Map<String, String> pohjakoulutusvaatimus;
    private String koulutuskoodi;
    private Date koulutuksenAlkamisPvmMin = null;
    private Date koulutuksenAlkamisPvmMax = null;
    private ArrayList<String> tarjoajat;
    private KoulutusmoduuliTyyppi koulutusmoduuliTyyppi;

    private String komoOid;

    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @return the nimi
     */
    public Map<String, String> getNimi() {
        return nimi;
    }

    /**
     * @param nimi the nimi to set
     */
    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    /**
     * @return the kausi
     */
    public Map<String, String> getKausi() {
        return kausi;
    }

    /**
     * @param kausi the kausi to set
     */
    public void setKausi(Map<String, String> kausi) {
        this.kausi = kausi;
    }

    /**
     * @return the kausiUri
     */
    public String getKausiUri() {
        return kausiUri;
    }

    /**
     * @param kausiUri the kausiUri to set
     */
    public void setKausiUri(String kausiUri) {
        this.kausiUri = kausiUri;
    }

    /**
     * @return the vuosi
     */
    public Integer getVuosi() {
        return vuosi;
    }

    /**
     * @param vuosi the vuosi to set
     */
    public void setVuosi(Integer vuosi) {
        this.vuosi = vuosi;
    }

    /**
     * @return the koulutusLaji
     */
    public Map<String, String> getKoulutusLaji() {
        return koulutusLaji;
    }

    /**
     * @param koulutusLaji the koulutusLaji to set
     */
    public void setKoulutusLaji(Map<String, String> koulutusLaji) {
        this.koulutusLaji = koulutusLaji;
    }

    /**
     * @return the koulutuslajiUri
     */
    public String getKoulutuslajiUri() {
        return koulutuslajiUri;
    }

    /**
     * @param koulutuslajiUri the koulutuslajiUri to set
     */
    public void setKoulutuslajiUri(String koulutuslajiUri) {
        this.koulutuslajiUri = koulutuslajiUri;
    }

    /**
     * @return the tila
     */
    public TarjontaTila getTila() {
        return tila;
    }

    /**
     * @param tila the tila to set
     */
    public void setTila(TarjontaTila tila) {
        this.tila = tila;
    }

    /**
     * @return the toteutustyyppiEnum
     */
    public ToteutustyyppiEnum getToteutustyyppiEnum() {
        return toteutustyyppiEnum;
    }

    /**
     * @param toteutustyyppiEnum the toteutustyyppiEnum to set
     */
    public void setToteutustyyppiEnum(ToteutustyyppiEnum toteutustyyppiEnum) {
        this.toteutustyyppiEnum = toteutustyyppiEnum;
    }

    /**
     * @return the pohjakoulutusvaatimus
     */
    public Map<String, String> getPohjakoulutusvaatimus() {
        return pohjakoulutusvaatimus;
    }

    /**
     * @param pohjakoulutusvaatimus the pohjakoulutusvaatimus to set
     */
    public void setPohjakoulutusvaatimus(Map<String, String> pohjakoulutusvaatimus) {
        this.pohjakoulutusvaatimus = pohjakoulutusvaatimus;
    }

    /**
     * @return the koulutuskoodi
     */
    public String getKoulutuskoodi() {
        return koulutuskoodi;
    }

    /**
     * @param koulutuskoodi the koulutuskoodi to set
     */
    public void setKoulutuskoodi(String koulutuskoodi) {
        this.koulutuskoodi = koulutuskoodi;
    }

    /**
     * @return the koulutuksenAlkamisPvmMin
     */
    public Date getKoulutuksenAlkamisPvmMin() {
        return koulutuksenAlkamisPvmMin;
    }

    /**
     * @param koulutuksenAlkamisPvmMin the koulutuksenAlkamisPvmMin to set
     */
    public void setKoulutuksenAlkamisPvmMin(Date koulutuksenAlkamisPvmMin) {
        this.koulutuksenAlkamisPvmMin = koulutuksenAlkamisPvmMin;
    }

    /**
     * @return the koulutuksenAlkamisPvmMax
     */
    public Date getKoulutuksenAlkamisPvmMax() {
        return koulutuksenAlkamisPvmMax;
    }

    /**
     * @param koulutuksenAlkamisPvmMax the koulutuksenAlkamisPvmMax to set
     */
    public void setKoulutuksenAlkamisPvmMax(Date koulutuksenAlkamisPvmMax) {
        this.koulutuksenAlkamisPvmMax = koulutuksenAlkamisPvmMax;
    }

    /**
     * @return the tarjoajat
     */
    public ArrayList<String> getTarjoajat() {
        return tarjoajat;
    }

    /**
     * @param tarjoajat the tarjoajat to set
     */
    public void setTarjoajat(ArrayList<String> tarjoajat) {
        this.tarjoajat = tarjoajat;
    }

    /**
     * @return the koulutusmoduuliTyyppi
     */
    public KoulutusmoduuliTyyppi getKoulutusmoduuliTyyppi() {
        return koulutusmoduuliTyyppi;
    }

    /**
     * @param koulutusmoduuliTyyppi the koulutusmoduuliTyyppi to set
     */
    public void setKoulutusmoduuliTyyppi(KoulutusmoduuliTyyppi koulutusmoduuliTyyppi) {
        this.koulutusmoduuliTyyppi = koulutusmoduuliTyyppi;
    }

    /**
     * @return the komoOid
     */
    public String getKomoOid() {
        return komoOid;
    }

    /**
     * @param komoOid the komoOid to set
     */
    public void setKomoOid(String komoOid) {
        this.komoOid = komoOid;
    }

}
