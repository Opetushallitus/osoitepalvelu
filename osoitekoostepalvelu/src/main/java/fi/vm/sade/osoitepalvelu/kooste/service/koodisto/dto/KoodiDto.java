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

import org.joda.time.LocalDate;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.helpers.KoodistoDateHelper;

public class KoodiDto implements Serializable {
    private String koodiUri;
    private long versio; // Koodin versio
    private String koodiArvo;
    // Koodiston tyyppi
    private KoodistoDto koodisto;

    private LocalDate voimassaAlkuPvm;
    private LocalDate voimassaLoppuPvm;
    private KoodistoTila tila;

    // Todellinen koodin data: Sisältää useita arvoja eri lokaaleille
    private List<KoodiArvoDto> metadata = new ArrayList<KoodiArvoDto>();

    public KoodiDto() {
    }

    public KoodiDto(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }

    public String getKoodiUri() {
        return koodiUri;
    }

    public void setKoodiUri(String koodiUri) {
        this.koodiUri = koodiUri;
    }

    public long getVersio() {
        return versio;
    }

    public void setVersio(long versio) {
        this.versio = versio;
    }

    public String getKoodiArvo() {
        return koodiArvo;
    }

    public void setKoodiArvo(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }

    public List<KoodiArvoDto> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<KoodiArvoDto> metadata) {
        this.metadata = metadata;
    }

    public KoodistoDto getKoodisto() {
        return koodisto;
    }

    public void setKoodisto(KoodistoDto koodisto) {
        this.koodisto = koodisto;
    }

    public KoodiArvoDto getArvoByKieli(String kieli) {
        for (KoodiArvoDto arvo : metadata) {
            if (arvo.getKieli().equalsIgnoreCase(kieli)) {
                return arvo;
            }
        }
        return null;
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

    public KoodiDto add(KoodiArvoDto arvo) {
        this.metadata.add(arvo);
        return this;
    }
}
