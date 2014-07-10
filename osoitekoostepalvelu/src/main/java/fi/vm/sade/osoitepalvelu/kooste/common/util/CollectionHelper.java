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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
public final class CollectionHelper {

    private CollectionHelper() {
    }

    /**
     * @param c collection to split
     * @param chunkSize the maximum number of elements in each chunk
     * @param <T> contained in the collection
     * @param <C> the collection type
     * @return a list of chunks
     */
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

    /**
     * @param col to filter using Collections2.filter
     * @param predicate to use
     * @param fallbackPredicate to use if predicate resulted in empty collection
     * @param <T> type contained in the collection
     * @return the filtered collection
     */
    public static <T> Collection<T> filter(Collection<T> col, Predicate<? super T> predicate,
                                           Predicate<? super T> fallbackPredicate ) {
        Collection<T> result = Collections2.filter(col, predicate);
        if (result.isEmpty()) {
            return Collections2.filter(col, fallbackPredicate);
        }
        return result;
    }

    /**
     * @param collection to collect extracted values from
     * @param extractor to transform items of collection to collections to combine
     * @param <E> the original container type
     * @param <T> the extracted item type
     * @return the extracted items in a single list
     */
    public static <E,T, C extends Collection<T>> List<T> collect(Collection<E> collection, Function<E,C> extractor) {
        List<T> results = new ArrayList<T>();
        for (E item : collection) {
            results.addAll(extractor.apply(item));
        }
        return results;
    }

}
