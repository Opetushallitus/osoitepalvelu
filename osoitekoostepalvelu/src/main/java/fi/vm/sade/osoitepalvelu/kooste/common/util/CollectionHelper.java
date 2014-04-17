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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
public final class CollectionHelper {

    private CollectionHelper() {
    }

    public static<T,C extends Collection<? extends T>> List<List<T>> split(C c, int chunkSize) {
        List<List<T>> ready = new ArrayList<List<T>>();
        List<T> current = new ArrayList<T>();
        if (c != null) {
            for (T e : c) {
                if (current.size() >= chunkSize) {
                    ready.add(current);
                    current = new ArrayList<T>();
                }
                current.add(e);
            }
        }
        ready.add(current);
        return ready;
    }
}
