package fi.vm.sade.osoitepalvelu.kooste.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public final class OpetuskieliHelper {

    private OpetuskieliHelper() {
    }

    public static Set<String> opetuskieliToKielet(String opetuskieli) {
        switch (opetuskieli) {
            case "oppilaitoksenopetuskieli_3#1": // suomi/ruotsi
                return Stream.of("kieli_fi", "kieli_sv")
                        .collect(collectingAndThen(toCollection(LinkedHashSet::new), Collections::unmodifiableSet));
            case "oppilaitoksenopetuskieli_1#1": // suomi
            case "oppilaitoksenopetuskieli_5#1": // saame
                return singleton("kieli_fi");
            case "oppilaitoksenopetuskieli_2#1": // ruotsi
                return singleton("kieli_sv");
            case "oppilaitoksenopetuskieli_4#1": // englanti
            case "oppilaitoksenopetuskieli_9#1": // muu
                return singleton("kieli_en");
            default:
                throw new IllegalArgumentException(opetuskieli);
        }
    }

    public static Set<String> opetuskieletToKielet(Collection<String> opetuskielet) {
        return opetuskielet.stream()
                .flatMap(opetuskieli -> opetuskieliToKielet(opetuskieli).stream())
                .collect(toCollection(LinkedHashSet::new));
    }

}
