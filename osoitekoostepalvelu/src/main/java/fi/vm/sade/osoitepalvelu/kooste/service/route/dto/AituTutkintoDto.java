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

import com.google.common.base.Predicate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 4/9/14
 * Time: 5:35 PM
 */
public class AituTutkintoDto implements Serializable {
    private static final int HASH_FACTOR = 31;
    private static final long serialVersionUID = 6027647418933397515L;

    @Indexed
    private String tutkintotunnus;
    @Indexed
    private String opintoalatunnus;
    private String vastuuhenkilo;
    private String sahkoposti_vastuuhenkilo;
    private String varavastuuhenkilo;
    private String sahkoposti_varavastuuhenkilo;

    public final static Predicate<AituTutkintoDto> WITH_VASTUUHENKILO = new Predicate<AituTutkintoDto>() {
        public boolean apply(AituTutkintoDto tutkinto) {
            return tutkinto.getVastuuhenkilo() != null && tutkinto.getVastuuhenkilo().trim().length() > 0;
        }
    };

    public String getTutkintotunnus() {
        return tutkintotunnus;
    }

    public void setTutkintotunnus(String tutkintotunnus) {
        this.tutkintotunnus = tutkintotunnus;
    }

    public String getOpintoalatunnus() {
        return opintoalatunnus;
    }

    public void setOpintoalatunnus(String opintoalatunnus) {
        this.opintoalatunnus = opintoalatunnus;
    }

    public String getVastuuhenkilo() {
        return vastuuhenkilo;
    }

    public void setVastuuhenkilo(String vastuuhenkilo) {
        this.vastuuhenkilo = vastuuhenkilo;
    }

    public String getSahkoposti_vastuuhenkilo() {
        return sahkoposti_vastuuhenkilo;
    }

    public void setSahkoposti_vastuuhenkilo(String sahkoposti_vastuuhenkilo) {
        this.sahkoposti_vastuuhenkilo = sahkoposti_vastuuhenkilo;
    }

    public String getVaravastuuhenkilo() {
        return varavastuuhenkilo;
    }

    public void setVaravastuuhenkilo(String varavastuuhenkilo) {
        this.varavastuuhenkilo = varavastuuhenkilo;
    }

    public String getSahkoposti_varavastuuhenkilo() {
        return sahkoposti_varavastuuhenkilo;
    }

    public void setSahkoposti_varavastuuhenkilo(String sahkoposti_varavastuuhenkilo) {
        this.sahkoposti_varavastuuhenkilo = sahkoposti_varavastuuhenkilo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AituTutkintoDto)) {
            return false;
        }

        AituTutkintoDto that = (AituTutkintoDto) o;

        if (opintoalatunnus != null ? !opintoalatunnus.equals(that.opintoalatunnus) : that.opintoalatunnus != null) {
            return false;
        }
        if (tutkintotunnus != null ? !tutkintotunnus.equals(that.tutkintotunnus) : that.tutkintotunnus != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tutkintotunnus != null ? tutkintotunnus.hashCode() : 0;
        result = HASH_FACTOR * result + (opintoalatunnus != null ? opintoalatunnus.hashCode() : 0);
        return result;
    }
}
