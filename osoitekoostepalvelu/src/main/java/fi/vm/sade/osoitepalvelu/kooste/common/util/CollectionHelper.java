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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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
                                           Predicate<? super T> fallbackPredicate){
        Collection<T> result = Collections2.filter(col, predicate);
        if (result.isEmpty()) {
            return Collections2.filter(col, fallbackPredicate);
        }
        return result;
    }

    /**
     * @param original function, result of which to filter
     * @param filter to apply to function results
     * @param <E>
     * @param <T>
     * @param <C>
     * @return a function that wraps the given original function in a function that filters the result of it with
     * given filter
     */
    public static <E, T, C extends List<T>> Function<E, List<T>> filter(final Function<E, C> original,
                                                                        final Predicate<T> filter) {
        return new Function<E, List<T>>() {
            public List<T> apply(E input) {
                return new ArrayList<T>(Collections2.filter(original.apply(input), filter));
            }
        };
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

    /**
     * @param map to collect
     * @param keys to collect from map
     * @param to add each values for given keys from given map
     * @param <K> key type
     * @param <T> value type
     * @param <C> collectin type to collect result to
     * @return the provided to collection with given keys from map appended
     */
    public static <K,T, C extends Collection<? super T>> C collect(Map<K,T> map, Collection<K> keys, C to) {
        for (K key : keys) {
            to.add(map.get(key));
        }
        return to;
    }

    /**
     * @param collection to map
     * @param map to add collection values to
     * @param keyExtractor extracts key from collection items
     * @param <K> key type
     * @param <T> collection item type
     * @param <C> collection type
     * @param <M> map type
     * @return the provided map with collection items mapped with their extracted keys so that first item with unique key
     * is included in the result map
     */
    public static <K, T, C extends Collection<T>, M extends Map<K,T>> M singleMap(C collection, M map,
                                              Function<T,K> keyExtractor) {
        for (T item : collection) {
            K key = keyExtractor.apply(item);
            if (!map.containsKey(key)) {
                map.put(key, item);
            }
        }
        return map;
    }

    /**
     * @param values1 to intersect with values2
     * @param values2 to intersect with values1
     * @param <T> type
     * @return the intersection of values1 and values2 with unique values ordered by values1
     */
    public static <T> List<T> intersection(Collection<T> values1, Collection<T> values2) {
        Set<T>  added = new HashSet<T>(),
                set2 = new HashSet<T>(values2);
        List<T> results = new ArrayList<T>();
        for (T value : values1) {
            if (set2.contains(value) && !added.contains(value)) {
                results.add(value);
                added.add(value);
            }
        }
        return results;
    }

    /**
     * @param c1 to merge with c2
     * @param c2 to merge with c1
     * @param keyExtractor to extract the unique (and comparable) merging key with
     * @param <K> key type
     * @param <T> value type
     * @return list of merged elements in c1 and c2 unique by key
     */
    public static <K, T> List<T> mergeIntersected(Collection<T> c1, Collection<T> c2,
                                                          Function<T,K> keyExtractor) {
        Map<K, T>   items1byKeys = CollectionHelper.singleMap(c1, new LinkedHashMap<K, T>(), keyExtractor),
                    items2byKeys = CollectionHelper.singleMap(c2, new LinkedHashMap<K, T>(), keyExtractor);
        return CollectionHelper.collect(items1byKeys,
                CollectionHelper.intersection(items1byKeys.keySet(), items2byKeys.keySet()),
                new ArrayList<T>());
    }
}
