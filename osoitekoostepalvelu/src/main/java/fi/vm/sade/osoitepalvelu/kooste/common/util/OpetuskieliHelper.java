package fi.vm.sade.osoitepalvelu.kooste.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public final class OpetuskieliHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpetuskieliHelper.class);

    private OpetuskieliHelper() {
    }

    public static Set<String> opetuskieliToKielet(String opetuskieli) {
        switch (opetuskieli.replaceFirst("#.*$", "")) {
            case "oppilaitoksenopetuskieli_3": // suomi/ruotsi
                return Stream.of("kieli_fi", "kieli_sv")
                        .collect(collectingAndThen(toCollection(LinkedHashSet::new), Collections::unmodifiableSet));
            case "oppilaitoksenopetuskieli_1": // suomi
            case "oppilaitoksenopetuskieli_5": // saame
                return singleton("kieli_fi");
            case "oppilaitoksenopetuskieli_2": // ruotsi
                return singleton("kieli_sv");
            case "oppilaitoksenopetuskieli_4": // englanti
            case "oppilaitoksenopetuskieli_9": // muu
                return singleton("kieli_en");
            default:
                LOGGER.warn("Tuntematon opetuskieli {}", opetuskieli);
                return emptySet();
        }
    }

    public static Set<String> opetuskieletToKielet(Collection<String> opetuskielet) {
        return opetuskielet.stream()
                .flatMap(opetuskieli -> opetuskieliToKielet(opetuskieli).stream())
                .collect(toCollection(LinkedHashSet::new));
    }

}
