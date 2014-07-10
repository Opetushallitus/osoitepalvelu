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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituSopimusDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituTutkintoDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 7/10/14
 * Time: 2:53 PM
 */
public class AituOppilaitosCriteria implements Serializable {
    private List<String> oppilaitoskoodiIn = new ArrayList<String>();
    private List<String> opintoalaTunnusIn = new ArrayList<String>();
    private List<String> tutkintoTunnusIn = new ArrayList<String>();

    public boolean isTutkintoUsed() {
        return this.tutkintoTunnusIn != null && !this.tutkintoTunnusIn.isEmpty();
    }

    public boolean isOpintoalaUsed() {
        return this.opintoalaTunnusIn != null && !this.opintoalaTunnusIn.isEmpty();
    }

    public boolean isOppilaitoskoodiUsed() {
        return this.oppilaitoskoodiIn != null && !this.oppilaitoskoodiIn.isEmpty();
    }

    public List<String> getOppilaitoskoodiIn() {
        return oppilaitoskoodiIn;
    }

    public void setOppilaitoskoodiIn(List<String> oppilaitoskoodiIn) {
        this.oppilaitoskoodiIn = oppilaitoskoodiIn;
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

    public Predicate<AituSopimusDto> createSopimusPredicate() {
        AndPredicateAdapter<AituSopimusDto> and = new AndPredicateAdapter<AituSopimusDto>();
        if (isOpintoalaUsed()) {
            and = and.and(new Predicate<AituSopimusDto>() {
                public boolean apply(AituSopimusDto sopimus) {
                    for (AituTutkintoDto tutkinto : sopimus.getTutkinnot()) {
                        if (getOpintoalaTunnusIn().contains(tutkinto.getOpintoalatunnus())) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        if (isTutkintoUsed()) {
            and = and.and(new Predicate<AituSopimusDto>() {
                public boolean apply(AituSopimusDto sopimus) {
                    for (AituTutkintoDto tutkinto : sopimus.getTutkinnot()) {
                        if (getTutkintoTunnusIn().contains(tutkinto.getTutkintotunnus())) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        return and;
    }
}
