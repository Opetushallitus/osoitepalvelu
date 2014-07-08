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

package fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria;

import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituKielisyys;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 4:49 PM
 */
public class AituToimikuntaCriteria implements Serializable {
    private static final long serialVersionUID = 5633851719629940782L;
    
    private List<String> kielisyysIn = new ArrayList<String>();
    private List<String> jasenKielisyysIn = new ArrayList<String>();
    private List<String> jasensInRoolis = new ArrayList<String>();
    private List<String> opintoalaTunnusIn = new ArrayList<String>();
    private List<String> tutkintoTunnusIn = new ArrayList<String>();
    private List<String> idsIn = new ArrayList<String>();
    private List<AituToimikunta.AituToimikausi> toimikausisIn = new ArrayList<AituToimikunta.AituToimikausi>();
    private boolean onlyVoimassaOlevat = true; // Jäsenyydet
    private List<String> organisaatioOidsIn = new ArrayList<String>();

    public boolean isIdsUsed() {
        return idsIn != null && !idsIn.isEmpty();
    }

    public boolean isKielisyysUsed() {
        return kielisyysIn != null && !kielisyysIn.isEmpty();
    }

    public boolean isJasenKielisyysUsed() {
        return jasenKielisyysIn != null && !jasenKielisyysIn.isEmpty();
    }

    public boolean isTutkintoUsed() {
        return this.tutkintoTunnusIn != null && !this.tutkintoTunnusIn.isEmpty();
    }

    public boolean isOpintoalaUsed() {
        return this.opintoalaTunnusIn != null && !this.opintoalaTunnusIn.isEmpty();
    }

    public boolean isToimikausiUsed() {
        return this.toimikausisIn != null && !this.toimikausisIn.isEmpty();
    }

    public boolean isOrganisaatioUsed() {
        return this.organisaatioOidsIn != null && !this.organisaatioOidsIn.isEmpty();
    }

    public List<String> getKielisyysIn() {
        return kielisyysIn;
    }

    public void setKielisyysIn(List<String> kielisyysIn) {
        this.kielisyysIn = kielisyysIn;
    }

    public List<String> getJasensInRoolis() {
        return jasensInRoolis;
    }

    public boolean isJasenRoolisUsed() {
        return jasensInRoolis != null && !jasensInRoolis.isEmpty();
    }

    public void setJasensInRoolis(List<String> jasensInRoolis) {
        this.jasensInRoolis = jasensInRoolis;
    }

    public List<String> getIdsIn() {
        return idsIn;
    }

    public void setIdsIn(List<String> idsIn) {
        this.idsIn = idsIn;
    }

    public List<AituToimikunta.AituToimikausi> getToimikausisIn() {
        return toimikausisIn;
    }

    public void setToimikausisIn(List<AituToimikunta.AituToimikausi> toimikausisIn) {
        this.toimikausisIn = toimikausisIn;
    }

    public boolean isOnlyVoimassaOlevat() {
        return onlyVoimassaOlevat;
    }

    public void setOnlyVoimassaOlevat(boolean onlyVoimassaOlevat) {
        this.onlyVoimassaOlevat = onlyVoimassaOlevat;
    }

    public List<String> getOpintoalaTunnusIn() {
        return opintoalaTunnusIn;
    }

    public void setOpintoalaTunnusIn(List<String> opintoalaTunnusIn) {
        this.opintoalaTunnusIn = opintoalaTunnusIn;
    }

    public List<String> getTutkintoTunnusIn() {
        return tutkintoTunnusIn;
    }

    public void setTutkintoTunnusIn(List<String> tutkintoTunnusIn) {
        this.tutkintoTunnusIn = tutkintoTunnusIn;
    }

    public List<String> getJasenKielisyysIn() {
        return jasenKielisyysIn;
    }

    public void setJasenKielisyysIn(List<String> jasenKielisyysIn) {
        this.jasenKielisyysIn = jasenKielisyysIn;
    }

    public void setOrganisaatioOidsIn(List<String> organisaatioOidsIn) {
        this.organisaatioOidsIn = organisaatioOidsIn;
    }

    public List<String> getOrganisaatioOidsIn() {
        return organisaatioOidsIn;
    }
}
