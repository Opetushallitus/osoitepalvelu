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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: ratamaa
 * Date: 4/1/14
 * Time: 1:08 PM
 */
public class Combiner<ResultType> {

    public interface Creator<ResultType> {
        ResultType create(PullSource src);
    }

    private Creator<ResultType> creator;
    private PullSource set = new PullSource();
    private boolean atLeastOne=false;

    public Combiner(Creator<ResultType> creator) {
        this.creator = creator;
    }

    public<T,E extends T, C extends Collection<E>> Combiner<ResultType> combinedWith(Class<T> clz, C values) {
        this.set.withList(clz, values, true, false);
        return this;
    }

    public<T,E extends T, C extends Collection<E>> Combiner<ResultType> withRepeated(Class<T> clz, C values) {
        this.set.withList(clz, values, false, true);
        return this;
    }

    public<T,E extends T, C extends Collection<E>> Combiner<ResultType> with(Class<T> clz, C values) {
        this.set.withList(clz, values, false, false);
        return this;
    }

    public Combiner<ResultType> atLeastOne() {
        atLeastOne = true;
        return this;
    }

    public<C extends Collection<ResultType>> C to(C results) {
        if (atLeastOne) {
            do {
                results.add(creator.create(this.set));
                this.set.next();
            } while (!this.set.isEnded());
        } else {
            while (!this.set.isEnded()) {
                results.add(creator.create(this.set));
                this.set.next();
            }
        }
        return results;
    }

    public static class PullSource {
        private MultiValueMap<Class<?>, List<?>> values = new LinkedMultiValueMap<Class<?>, List<?>>();
        private Map<Class<?>, AtomicInteger> typeIndex = new HashMap<Class<?>, AtomicInteger>();
        private MultiValueMap<Class<?>,Counter> indexes = new LinkedMultiValueMap<Class<?>, Counter>();
        private List<Counter> counters = new ArrayList<Counter>();
        private Counter previousCombinedCounter;

        private static final class Counter {
            private boolean repeated;
            private boolean combined;
            private int count;
            private Counter previousCombined;
            private AtomicInteger counter = new AtomicInteger(0);
            private boolean ended = false;

            private Counter(int count, boolean combine, boolean repeated) {
                this.count = count;
                this.combined = combine;
                this.repeated = repeated;
            }

            private void increment() {
                if (this.counter.get() < count) {
                    counter.incrementAndGet();
                }
                if (this.counter.get() >= count) {
                    if (this.repeated) {
                        this.ended = true;
                        this.reset();
                    }
                    if (this.isCombined() && previousCombined != null) {
                        if (!this.previousCombined.isAllPreviousAtLastOrEnded()) {
                            this.reset();
                        }
                        this.previousCombined.increment();
                    }
                }
            }

            private void reset() {
                counter.set(0);
            }

            private boolean isCombined() {
                return combined;
            }

            private boolean isAtLastOrEnded() {
                return this.counter.get() >= count-1;
            }

            private boolean isEnded() {
                return this.counter.get() >= count || ended;
            }

            private Integer get() {
                return counter.get();
            }

            private boolean isAllPreviousAtLastOrEnded() {
                if (!this.isAtLastOrEnded()) {
                    return false;
                }
                if (this.previousCombined != null) {
                    return this.previousCombined.isAllPreviousAtLastOrEnded();
                }
                return true;
            }

            @Override
            public String toString() {
                return (1+this.counter.get()) + " / " + count;
            }
        }

        protected PullSource() {
        }

        protected void next() {
            for (Counter c : counters) {
                if (!c.isCombined()) {
                    c.increment();
                }
            }
            if (this.previousCombinedCounter != null) {
                this.previousCombinedCounter.increment();
            }
        }

        @Override
        public String toString() {
            List<String> strs = new ArrayList<String>();
            for (Counter c : counters) {
                strs.add(c.toString());
            }
            return StringHelper.join(", ", strs.toArray(new String[0]));
        }

        protected<T,E extends T, C extends Collection<E>> PullSource withList(Class<T> key, C values,
                                                              boolean combined, boolean repeated) {
            this.values.add(key, new ArrayList<Object>(values));
            typeIndex.put(key, new AtomicInteger(0));
            // Sonar ei tunnista, että MultiValueMap palauttaa listan.
            List<Counter> indexes = this.indexes.get(key); // NOSONAR
            if (indexes == null) {
                indexes = new ArrayList<Counter>();
                this.indexes.put(key, indexes);
            }
            Counter counter = new Counter(values.size(), combined, repeated);
            indexes.add(counter);
            counters.add(counter);
            if (combined) {
                counter.previousCombined = previousCombinedCounter;
                previousCombinedCounter = counter;
            }
            return this;
        }

        protected boolean isEnded() {
            for (Counter c : counters) {
                if (!c.isEnded()) {
                     return false;
                }
            }
            return true;
        }

        @SuppressWarnings("unchecked")
        public<T extends RequestedType,RequestedType> Optional<T> get(Class<RequestedType> type) {
            List<List<?>> typeValues = values.get(type);
            if (typeValues == null) {
                return Optional.absent();
            }
            int typeIndex = this.typeIndex.get(type).getAndAdd(1),
                realTypeIndex = typeIndex % typeValues.size();
            List<?> currentValues = typeValues.get(realTypeIndex);
            // Sonar ei tunnista, että MultiValueMap palauttaa listan.
            Integer i = indexes.get(type).get(realTypeIndex).get(); // NOSONAR
            if (i >= currentValues.size()) {
                return Optional.absent();
            }
            return (Optional<T>) Optional.of(currentValues.get(i));
        }
    }

}
