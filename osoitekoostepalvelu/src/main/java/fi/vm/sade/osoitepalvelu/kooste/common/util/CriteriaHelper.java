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

package fi.vm.sade.osoitepalvelu.kooste.common.util;

import com.google.common.base.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ratamaa
 * Date: 5/12/14
 * Time: 10:25 AM
 */
public final class CriteriaHelper {

    private static final Pattern KOODI_WITH_VERSION = Pattern.compile("(.+?)#(\\d+)");
    private static final String OPTIONAL_KOODI_VERSION_POSTFIX = "(#(\\d+))?";

    private CriteriaHelper() {
    }

    public static Optional<Criteria> inAnyKoodiVersion(Criteria c, String key, Collection<String> koodis) {
        if (koodis == null || koodis.isEmpty()) {
            return Optional.absent();
        }

        Criteria[] criteria = new Criteria[koodis.size()];
        int i = 0;
        for (String koodi : koodis) {
            // Strip the version part if specified:
            Matcher m = KOODI_WITH_VERSION.matcher(koodi);
            if (m.find()) {
                koodi = m.group(1);
            }
            criteria[i++] = Criteria.where(key).regex(Pattern.compile(koodi + OPTIONAL_KOODI_VERSION_POSTFIX));
        }
        return Optional.of(c.orOperator(criteria));
    }

    public static Optional<Criteria> inParentOids(Criteria c, String key, Collection<String> parentOids) {
        if (parentOids == null || parentOids.isEmpty()) {
            return Optional.absent();
        }

        Criteria[] criteria = new Criteria[parentOids.size()];
        int i = 0;
        for (String oid : parentOids) {
            criteria[i++] = Criteria.where(key).regex(Pattern.compile("\\|"+oid+"\\|"));
        }
        return Optional.of(c.orOperator(criteria));
    }

    public static final class Conditions {
        private List<Criteria> criteria = new ArrayList<Criteria>();

        public Conditions(Criteria... c) {
            this.criteria.addAll(Arrays.asList(c));
        }

        public Conditions add(Criteria c) {
            this.criteria.add(c);
            return this;
        }

        public Conditions add(Optional<Criteria> c) {
            if (c.isPresent()) {
                this.criteria.add(c.get());
            }
            return this;
        }

        public Criteria applyTo(Criteria c) {
            if (criteria.isEmpty()) {
                return c;
            }
            return c.andOperator(criteria.toArray(new Criteria[0]));
        }
    }
}
