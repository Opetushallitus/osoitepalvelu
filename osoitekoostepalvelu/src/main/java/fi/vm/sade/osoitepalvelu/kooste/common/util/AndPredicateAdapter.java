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
import java.util.List;

import com.google.common.base.Predicate;

/**
 * User: ratamaa
 * Date: 3/24/14
 * Time: 11:05 AM
 */
public class AndPredicateAdapter<T> implements Predicate<T> {
    private List<Predicate<T>> predicates;

    public AndPredicateAdapter() {
        this.predicates  =  new ArrayList<Predicate<T>>();
    }

    public AndPredicateAdapter(List<Predicate<T>> predicates) {
        this.predicates  =  new ArrayList<Predicate<T>>(predicates);
    }

    public AndPredicateAdapter(Predicate<T> predicate1, Predicate<T> predicate2) {
        this.predicates  =  new ArrayList<Predicate<T>>();
        this.predicates.add(predicate1);
        this.predicates.add(predicate2);
    }

    public AndPredicateAdapter(List<Predicate<T>> predicates, Predicate<T> additional) {
        this.predicates  =  new ArrayList<Predicate<T>>(predicates);
        this.predicates.add(additional);
    }

    public AndPredicateAdapter<T> and(Predicate<T> predicate) {
        return new AndPredicateAdapter<T>(this.predicates, predicate);
    }

    @Override
    public boolean apply(T object) {
        for (Predicate<T> predicate : predicates) {
            if (!predicate.apply(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AndPredicateAdapter)) {
            return false;
        }
        AndPredicateAdapter<?> other = (AndPredicateAdapter<?>)obj;
        return this.predicates.equals(other.predicates);
    }
    
    public int hashCode() {
        return super.hashCode();
    }
    
    public boolean isFiltering() {
        return !this.predicates.isEmpty();
    }
}
