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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 4:49 PM
 */
public class AituToimikuntaCriteria implements Serializable {
    private List<AituKielisyys> kielisyysIn = new ArrayList<AituKielisyys>();
    private List<String> jasensInRoolis = new ArrayList<String>();
    private List<String> idsIn = new ArrayList<String>();
    private boolean onlyVoimassaOlevat = true;

    public List<AituKielisyys> getKielisyysIn() {
        return kielisyysIn;
    }

    public boolean isKielisyysUsed() {
        return kielisyysIn != null && !kielisyysIn.isEmpty();
    }

    public void setKielisyysIn(List<AituKielisyys> kielisyysIn) {
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

    public boolean isIdsUsed() {
        return idsIn != null && !idsIn.isEmpty();
    }

    public boolean isOnlyVoimassaOlevat() {
        return onlyVoimassaOlevat;
    }

    public void setOnlyVoimassaOlevat(boolean onlyVoimassaOlevat) {
        this.onlyVoimassaOlevat = onlyVoimassaOlevat;
    }
}
