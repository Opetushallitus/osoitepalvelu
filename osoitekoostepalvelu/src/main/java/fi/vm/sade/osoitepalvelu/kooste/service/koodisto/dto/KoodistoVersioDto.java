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

import org.joda.time.LocalDate;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.helpers.KoodistoDateHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;

/**
 * Koodiston versiotietojen mallintaminen.
 */
public class KoodistoVersioDto {
    private String koodistoUri;
    private KoodistoTyyppi koodistoTyyppi;
    private long versio;
    private LocalDate voimassaAlkuPvm;
    private LocalDate voimassaLoppuPvm;

    private KoodistoTila tila;

    public String getKoodistoUri() {
        return koodistoUri;
    }

    public void setKoodistoUri(String koodistoUri) {
        this.koodistoUri = koodistoUri;
        if (koodistoUri != null) {
            this.koodistoTyyppi = KoodistoTyyppi.parseTyyppi(koodistoUri);
        }
    }

    public KoodistoTyyppi getKoodistoTyyppi() {
        return koodistoTyyppi;
    }

    public void setKoodistoTyyppi(KoodistoTyyppi koodistoTyyppi) {
        this.koodistoTyyppi = koodistoTyyppi;
    }

    public long getVersio() {
        return versio;
    }

    public void setVersio(long versio) {
        this.versio = versio;
    }

    public LocalDate getVoimassaAlkuPvm() {
        return voimassaAlkuPvm;
    }

    public void setVoimassaAlkuPvm(LocalDate voimassaAlkuPvm) {
        this.voimassaAlkuPvm = voimassaAlkuPvm;
    }

    public LocalDate getVoimassaLoppuPvm() {
        return voimassaLoppuPvm;
    }

    public void setVoimassaLoppuPvm(LocalDate voimassaLoppuPvm) {
        this.voimassaLoppuPvm = voimassaLoppuPvm;
    }

    public KoodistoTila getTila() {
        return tila;
    }

    public void setTila(KoodistoTila tila) {
        this.tila = tila;
    }

    public boolean isVoimassaPvm(LocalDate pvm) {
        return KoodistoDateHelper.isPaivaVoimassaValilla(pvm, voimassaAlkuPvm, voimassaLoppuPvm);
    }
}
