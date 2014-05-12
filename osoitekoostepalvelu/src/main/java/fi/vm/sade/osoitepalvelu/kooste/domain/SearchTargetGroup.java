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

package fi.vm.sade.osoitepalvelu.kooste.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:34 PM
 */
public class SearchTargetGroup implements Serializable {
    private static final long serialVersionUID  =  -1439905791723850160L;

    public enum GroupType {
        JARJESTAJAT_YLLAPITAJAT("Koulutustoimija"),
        OPPILAITOKSET("Oppilaitos"),
        OPETUSPISTEET("Opetuspiste"),
        OPPISOPIMUSTOIMPISTEET("Oppisopimustoimipiste"),
        MUUT_ORGANISAATIOT("Muu organisaatio"),
        TUTKINTOTOIMIKUNNAT,
        KOULUTA_KAYTTAJAT,
        AIPAL_KAYTTAJAT;

        private final String organisaatioPalveluTyyppiArvo;

        private GroupType() {
            this.organisaatioPalveluTyyppiArvo  =  null;
        }
        private GroupType(String organisaatioPalveluTyyppiArvo) {
            this.organisaatioPalveluTyyppiArvo  =  organisaatioPalveluTyyppiArvo;
        }

        public static GroupType[] getHenkiloHakuTypes() {
            return new GroupType[] {KOULUTA_KAYTTAJAT};
        }

        public static GroupType[] getAnyHenkiloTypes() {
            return new GroupType[] {KOULUTA_KAYTTAJAT, TUTKINTOTOIMIKUNNAT, KOULUTA_KAYTTAJAT, AIPAL_KAYTTAJAT};
        }

        public static GroupType[] getAnyOrganisaatioTypes() {
            return new GroupType[] {JARJESTAJAT_YLLAPITAJAT, OPPILAITOKSET, OPETUSPISTEET, OPPISOPIMUSTOIMPISTEET,
                    MUUT_ORGANISAATIOT, TUTKINTOTOIMIKUNNAT};
        }

        public String getOrganisaatioPalveluTyyppiArvo() {
            return organisaatioPalveluTyyppiArvo;
        }

        public static GroupType[] getOrganisaatioPalveluTypes() {
            List<GroupType> types  =  new ArrayList<GroupType>();
            for (GroupType type : values()) {
                if (type.getOrganisaatioPalveluTyyppiArvo() != null) {
                    types.add(type);
                }
            }
            return types.toArray(new GroupType[0]);
        }
    };

    public enum TargetType {
        ORGANISAATIO,
        YHTEYSHENKILO,
        REHTORI,
        KRIISITIEDOTUS,
        KOULUTUSNEVONTA,
        PUHEENJOHTAJA,
        SIHTEERI,
        JASENET,
        TUNNUKSENHALTIJAT;
    };

    private GroupType type;
    private List<TargetType> options  =  new ArrayList<TargetType>();

    public SearchTargetGroup() {
    }

    public SearchTargetGroup(GroupType type, List<TargetType> options) {
        this.type  =  type;
        this.options  =  options;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type  =  type;
    }

    public List<TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<TargetType> options) {
        this.options  =  options;
    }
}
