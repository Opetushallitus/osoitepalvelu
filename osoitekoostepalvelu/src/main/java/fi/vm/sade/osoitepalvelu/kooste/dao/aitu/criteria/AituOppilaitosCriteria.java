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
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituSopimusDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituTutkintoDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 7/10/14
 * Time: 2:53 PM
 */
public class AituOppilaitosCriteria implements Serializable, TutkintorakenneAwareCriteria {
    private static final long serialVersionUID = 4448829574717272869L;
    
    private List<String> oppilaitoskoodiIn = new ArrayList<String>();
    private List<String> opintoalaTunnusIn = new ArrayList<String>();
    private List<String> tutkintoTunnusIn = new ArrayList<String>();
    private List<String> toimikuntaIn = new ArrayList<String>();
    private List<AituToimikunta.AituToimikausi> toimikausisIn = new ArrayList<AituToimikunta.AituToimikausi>();

    @Override
    public boolean isTutkintoUsed() {
        return this.tutkintoTunnusIn != null && !this.tutkintoTunnusIn.isEmpty();
    }

    @Override
    public boolean isOpintoalaUsed() {
        return this.opintoalaTunnusIn != null && !this.opintoalaTunnusIn.isEmpty();
    }

    public boolean isOppilaitoskoodiUsed() {
        return this.oppilaitoskoodiIn != null && !this.oppilaitoskoodiIn.isEmpty();
    }

    public boolean isToimikuntaUsed() {
        return this.toimikuntaIn != null && !this.toimikuntaIn.isEmpty();
    }

    public boolean isToimikausiUsed() {
        return this.toimikausisIn != null && !this.toimikausisIn.isEmpty();
    }

    public List<String> getOppilaitoskoodiIn() {
        return oppilaitoskoodiIn;
    }

    public void setOppilaitoskoodiIn(List<String> oppilaitoskoodiIn) {
        this.oppilaitoskoodiIn = oppilaitoskoodiIn;
    }

    public List<String> getToimikuntaIn() {
        return toimikuntaIn;
    }

    public void setToimikuntaIn(List<String> toimikuntaIn) {
        this.toimikuntaIn = toimikuntaIn;
    }

    public void setToimikausisIn(List<AituToimikunta.AituToimikausi> toimikausisIn) {
        this.toimikausisIn = toimikausisIn;
    }

    public List<AituToimikunta.AituToimikausi> getToimikausisIn() {
        return toimikausisIn;
    }

    public boolean isRelatedToimikuntaResultsNeeded() {
        return isToimikausiUsed() || isToimikuntaUsed();
    }

    public AituToimikuntaCriteria toRelatedToimikuntaCriteria() {
        AituToimikuntaCriteria toimikuntaCriteria = new AituToimikuntaCriteria();
        toimikuntaCriteria.setToimikausisIn(this.toimikausisIn);
        if (toimikuntaCriteria.isToimikausiUsed()) {
            toimikuntaCriteria.setOnlyVoimassaOlevat(false);
        }
        toimikuntaCriteria.setIdsIn(this.toimikuntaIn);
        return toimikuntaCriteria;
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

    public AndPredicateAdapter<AituTutkintoDto> createTutkintoPredicate() {
        AndPredicateAdapter<AituTutkintoDto> and = new AndPredicateAdapter<AituTutkintoDto>();
        if (isOpintoalaUsed()) {
            and = and.and(new Predicate<AituTutkintoDto>() {
                public boolean apply(AituTutkintoDto tutkinto) {
                    return getOpintoalaTunnusIn().contains(tutkinto.getOpintoalatunnus());
                }
            });
        }
        if (isTutkintoUsed()) {
            and = and.and(new Predicate<AituTutkintoDto>() {
                public boolean apply(AituTutkintoDto tutkinto) {
                    return getTutkintoTunnusIn().contains(tutkinto.getTutkintotunnus());
                }
            });
        }
        return and;
    }

    public AndPredicateAdapter<AituSopimusDto> createSopimusPredicate(final List<String> matchedToimikuntas) {
        AndPredicateAdapter<AituSopimusDto> and = new AndPredicateAdapter<AituSopimusDto>();
        if (isToimikuntaUsed()) {
            and = and.and(new Predicate<AituSopimusDto>() {
                public boolean apply(AituSopimusDto sopimus) {
                    return getToimikuntaIn().contains(sopimus.getToimikunta());
                }
            });
        }
        final AndPredicateAdapter<AituTutkintoDto> tutkintoPredicate = createTutkintoPredicate();
        if (tutkintoPredicate.isFiltering()) {
            and = and.and(new Predicate<AituSopimusDto>() {
                public boolean apply(AituSopimusDto sopimus) {
                    for (AituTutkintoDto tutkinto : sopimus.getTutkinnot()) {
                        if (tutkintoPredicate.apply(tutkinto)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        if (isRelatedToimikuntaResultsNeeded() && matchedToimikuntas == null) {
            throw new IllegalStateException("Matched toimikuntas needed for sopimus matching!");
        }
        if (isToimikausiUsed()) {
            and = and.and(new Predicate<AituSopimusDto>() {
                public boolean apply(AituSopimusDto sopimus) {
                    return matchedToimikuntas.contains(sopimus.getToimikunta());
                }
            });
        }
        return and;
    }
}
