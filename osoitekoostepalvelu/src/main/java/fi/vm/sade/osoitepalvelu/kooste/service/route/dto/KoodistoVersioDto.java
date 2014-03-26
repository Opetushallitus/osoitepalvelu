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

import org.joda.time.LocalDate;

import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers.KoodistoDateHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;

import java.io.Serializable;

/**
 * Koodiston versiotietojen mallintaminen.
 */
public class KoodistoVersioDto implements Serializable {
    private static final long serialVersionUID = -6700268835522853874L;
    
    private static final int HASH_FACTOR = 31;
    private String koodistoUri;
    private KoodistoTyyppi koodistoTyyppi;
    private long versio;
    private LocalDate voimassaAlkuPvm;
    private LocalDate voimassaLoppuPvm;

    private KoodistoTila tila;

    public KoodistoVersioDto() {
    }

    public KoodistoVersioDto(String koodistoUri, KoodistoTyyppi koodistoTyyppi, long versio,
                             LocalDate voimassaAlkuPvm, LocalDate voimassaLoppuPvm, KoodistoTila tila) {
        this.koodistoUri  =  koodistoUri;
        this.koodistoTyyppi  =  koodistoTyyppi;
        this.versio  =  versio;
        this.voimassaAlkuPvm  =  voimassaAlkuPvm;
        this.voimassaLoppuPvm  =  voimassaLoppuPvm;
        this.tila  =  tila;
    }

    public String getKoodistoUri() {
        return koodistoUri;
    }

    public void setKoodistoUri(String koodistoUri) {
        this.koodistoUri = koodistoUri;
        if (koodistoUri != null) {
            this.koodistoTyyppi  =  KoodistoTyyppi.parseTyyppi(koodistoUri);
        }
    }

    public KoodistoTyyppi getKoodistoTyyppi() {
        return koodistoTyyppi;
    }

    public void setKoodistoTyyppi(KoodistoTyyppi koodistoTyyppi) {
        this.koodistoTyyppi  =  koodistoTyyppi;
    }

    public long getVersio() {
        return versio;
    }

    public void setVersio(long versio) {
        this.versio  =  versio;
    }

    public LocalDate getVoimassaAlkuPvm() {
        return voimassaAlkuPvm;
    }

    public void setVoimassaAlkuPvm(LocalDate voimassaAlkuPvm) {
        this.voimassaAlkuPvm  =  voimassaAlkuPvm;
    }

    public LocalDate getVoimassaLoppuPvm() {
        return voimassaLoppuPvm;
    }

    public void setVoimassaLoppuPvm(LocalDate voimassaLoppuPvm) {
        this.voimassaLoppuPvm  =  voimassaLoppuPvm;
    }

    public KoodistoTila getTila() {
        return tila;
    }

    public void setTila(KoodistoTila tila) {
        this.tila  =  tila;
    }

    public boolean isVoimassaPvm(LocalDate pvm) {
        return KoodistoDateHelper.isPaivaVoimassaValilla(pvm, voimassaAlkuPvm, voimassaLoppuPvm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { 
            return true;
        }
        if (o == null || getClass() != o.getClass()) { 
            return false;
        }

        KoodistoVersioDto that  =  (KoodistoVersioDto) o;

        if (versio != that.versio) { 
            return false;
        }
        if (koodistoTyyppi != that.koodistoTyyppi) { 
            return false;
        }
        if (koodistoUri != null ? !koodistoUri.equals(that.koodistoUri) : that.koodistoUri != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result  =  koodistoUri != null ? koodistoUri.hashCode() : 0;
        result  =  HASH_FACTOR * result  +  (koodistoTyyppi != null ? koodistoTyyppi.hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (int) (versio ^ (versio >>> (HASH_FACTOR + 1)));
        return result;
    }
}
