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

package fi.vm.sade.osoitepalvelu.kooste.dao.aitu;

import com.google.common.base.Optional;
import fi.vm.sade.osoitepalvelu.kooste.common.util.KoodiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 5:24 PM
 */
public enum AituKielisyys {
    kieli_fi("fi"),
    kieli_sv("sv"),
    kieli_2k("2k"),
    kieli_en("en");

    public static final String KOODISTO_TYYPPI_OPPILAITOKSEN_OPETUSKIELI_ARVO_PREFIX = "oppilaitoksenopetuskieli_";
    private String aituKieli;

    private AituKielisyys(String aituKieli) {
        this.aituKieli = aituKieli;
    }

    public static Optional<AituKielisyys> fromLocale(Locale locale) {
        if (locale == null) {
            return Optional.absent();
        }
        try {
            return Optional.of(valueOf("kieli_"+locale.getLanguage().toLowerCase()));
        } catch (IllegalStateException e) {
            return Optional.absent();
        }
    }

    public static List<AituKielisyys> fromOppilaitoksenOpetuskieliKoodistoValues(List<String> kielis){
        List<AituKielisyys> kielisyys = new ArrayList<AituKielisyys>();
        for (String kieli : kielis) {
            AituKielisyys aituKielisyys = fromOppilaitoksenOpetuskieliKoodiValue(kieli);
            if (kielisyys != null) {
                kielisyys.add(aituKielisyys);
            }
        }
        return kielisyys;
    }

    public static AituKielisyys fromOppilaitoksenOpetuskieliKoodiValue(String value) {
        if (value == null) {
            return null;
        }
        // TODO: Find a cleaner way to implement me!
        String numberPart = KoodiHelper.parseKoodiArvo(KOODISTO_TYYPPI_OPPILAITOKSEN_OPETUSKIELI_ARVO_PREFIX, value);
        if (numberPart == null || numberPart.equals(value)) {
            return null;
        }
        AituKielisyys[] values = AituKielisyys.values();
        try {
            int numberValue = Integer.parseInt(numberPart);
            if (numberValue < 0 || values.length <= numberValue) {
                return null;
            }
            return values[ numberValue-1 ];
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getAituKieli() {
        return aituKieli;
    }

    public static List<String> aituKielis(List<AituKielisyys> kielisyydet) {
        List<String> result = new ArrayList<String>();
        for (AituKielisyys kielisyys : kielisyydet) {
            if (kielisyys != null) {
                result.add(kielisyys.getAituKieli());
            }
        }
        return result;
    }
}
