package fi.vm.sade.osoitepalvelu.kooste.common.util;

import org.joda.time.LocalDate;

public final class DateHelper {

    private DateHelper() {
    }

    public static boolean isBetweenInclusive(LocalDate date, LocalDate start, LocalDate end) {
        return (start == null || start.isBefore(date) || start.isEqual(date))
                && (end == null || end.isAfter(date) || end.isEqual(date));
    }

}
