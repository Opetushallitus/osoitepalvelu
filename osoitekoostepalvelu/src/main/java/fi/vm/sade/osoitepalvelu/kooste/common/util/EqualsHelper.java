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

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:03 PM
 */
public class EqualsHelper {

    public static boolean differentNulls(Object x, Object y) {
        return (x == null && y != null) || (y == null && x != null);
    }

    public static boolean notNulls(Object x, Object y) {
        return (x != null && y != null);
    }

    public static boolean equals(Object x, Object y) {
        return x == y || (x != null && y != null && x.equals(y));
    }

    private Object[] state;

    public EqualsHelper(Object ...state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EqualsHelper)) {
            return false;
        }
        EqualsHelper other = (EqualsHelper) obj;
        if (other.state.length != this.state.length) {
            return false;
        }
        for (int i = 0; i < this.state.length; ++i) {
            if (!equals(this.state[i], other.state[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        // http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
        // In practice: Use the same set of fields that you use to compute equals() to compute hashCode().
        HashCodeBuilder hashBuilder = new HashCodeBuilder();
        hashBuilder.append(this.state.length);
        for (int i = 0; i < this.state.length; ++i) {
            hashBuilder.append(this.state[i]);
        }
        return hashBuilder.toHashCode();
    }
}
