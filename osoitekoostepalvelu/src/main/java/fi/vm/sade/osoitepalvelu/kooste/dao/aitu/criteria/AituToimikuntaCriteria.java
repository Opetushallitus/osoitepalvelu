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

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.AndPredicateAdapter;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituJasenyysDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 4:49 PM
 */
public class AituToimikuntaCriteria implements Serializable, TutkintorakenneAwareCriteria {
    private static final long serialVersionUID = 5633851719629940782L;

    private List<String> kielisyysIn = new ArrayList<String>();
    private List<String> jasenKielisyysIn = new ArrayList<String>();
    private List<String> jasensInRoolis = new ArrayList<String>();
    private List<String> opintoalaTunnusIn = new ArrayList<String>();
    private List<String> tutkintoTunnusIn = new ArrayList<String>();
    private List<String> idsIn = new ArrayList<String>();
    private List<AituToimikunta.AituToimikausi> toimikausisIn = new ArrayList<AituToimikunta.AituToimikausi>();
    private boolean onlyVoimassaOlevat = true; // Jäsenyydet
    private List<String> oppilaitoskoodiIn = new ArrayList<String>();

    private boolean viranomaisEmail = true; // viranomaissähköposti
    private boolean jasenet = true;         // Sähköpostiosoitteet

    public boolean isIdsUsed() {
        return idsIn != null && !idsIn.isEmpty();
    }

    public boolean isKielisyysUsed() {
        return kielisyysIn != null && !kielisyysIn.isEmpty();
    }

    public boolean isJasenKielisyysUsed() {
        return jasenKielisyysIn != null && !jasenKielisyysIn.isEmpty();
    }

    @Override
    public boolean isTutkintoUsed() {
        return this.tutkintoTunnusIn != null && !this.tutkintoTunnusIn.isEmpty();
    }

    @Override
    public boolean isOpintoalaUsed() {
        return this.opintoalaTunnusIn != null && !this.opintoalaTunnusIn.isEmpty();
    }

    public boolean isToimikausiUsed() {
        return this.toimikausisIn != null && !this.toimikausisIn.isEmpty();
    }

    public boolean isOppilaitoskoodiUsed() {
        return this.oppilaitoskoodiIn != null && !this.oppilaitoskoodiIn.isEmpty();
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

    @Override
    public List<String> getOpintoalaTunnusIn() {
        return opintoalaTunnusIn;
    }

    @Override
    public void setOpintoalaTunnusIn(List<String> opintoalaTunnusIn) {
        this.opintoalaTunnusIn = opintoalaTunnusIn;
    }

    @Override
    public List<String> getTutkintoTunnusIn() {
        return tutkintoTunnusIn;
    }

    @Override
    public void setTutkintoTunnusIn(List<String> tutkintoTunnusIn) {
        this.tutkintoTunnusIn = tutkintoTunnusIn;
    }

    public List<String> getJasenKielisyysIn() {
        return jasenKielisyysIn;
    }

    public void setJasenKielisyysIn(List<String> jasenKielisyysIn) {
        this.jasenKielisyysIn = jasenKielisyysIn;
    }

    public List<String> getOppilaitoskoodiIn() {
        return oppilaitoskoodiIn;
    }

    public void setOppilaitoskoodiIn(List<String> oppilaitoskoodiIn) {
        this.oppilaitoskoodiIn = oppilaitoskoodiIn;
    }

    public boolean isViranomaisEmail() {
        return viranomaisEmail;
    }

    public void setViranomaisEmail(boolean viranomaisEmail) {
        this.viranomaisEmail = viranomaisEmail;
    }

    public AituOppilaitosCriteria toOppilaitosCriteria() {
        AituOppilaitosCriteria oppilaitosCriteria = new AituOppilaitosCriteria();
        oppilaitosCriteria.setOppilaitoskoodiIn(this.oppilaitoskoodiIn);
        oppilaitosCriteria.setOpintoalaTunnusIn(this.opintoalaTunnusIn);
        oppilaitosCriteria.setTutkintoTunnusIn(this.tutkintoTunnusIn);
        return oppilaitosCriteria;
    }

    public AndPredicateAdapter<AituJasenyysDto> createJasenyysPredicate() {
        AndPredicateAdapter<AituJasenyysDto> and = new AndPredicateAdapter<AituJasenyysDto>();
        if (isJasenRoolisUsed()) {
            and = and.and(new Predicate<AituJasenyysDto>() {
                public boolean apply(AituJasenyysDto jasenyys) {
                    return getJasensInRoolis().contains(jasenyys.getRooli());
                }
            });
        }
        if (isJasenKielisyysUsed()) {
            and = and.and(new Predicate<AituJasenyysDto>() {
                public boolean apply(AituJasenyysDto jasenyys) {
                    return getJasenKielisyysIn().contains(jasenyys.getAidinkieli());
                }
            });
        }
        if (isOnlyVoimassaOlevat()) {
            and = and.and(new Predicate<AituJasenyysDto>() {
                public boolean apply(AituJasenyysDto jasenyys) {
                    return jasenyys.isVoimassa();
                }
            });
        }
        return and;
    }

    /**
     * @return the jasenet
     */
    public boolean isJasenet() {
        return jasenet;
    }

    /**
     * @param jasenet the jasenet to set
     */
    public void setJasenet(boolean jasenet) {
        this.jasenet = jasenet;
    }
}
