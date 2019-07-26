package fi.vm.sade.osoitepalvelu.kooste.common.util;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateHelperTest {

    @Test
    public void isBetweenInclusiveStartAndEndNull() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = null;
        LocalDate end = null;

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertTrue(result);
    }

    @Test
    public void isBetweenInclusiveDateAfterStart() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = new LocalDate(2019, 5, 7);
        LocalDate end = null;

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertTrue(result);
    }

    @Test
    public void isBetweenInclusiveDateEqualStart() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = new LocalDate(2019, 5, 8);
        LocalDate end = null;

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertTrue(result);
    }

    @Test
    public void isBetweenInclusiveDateBeforeStart() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = new LocalDate(2019, 5, 9);
        LocalDate end = null;

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertFalse(result);
    }

    @Test
    public void isBetweenInclusiveDateAfterEnd() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = null;
        LocalDate end = new LocalDate(2019, 5, 7);

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertFalse(result);
    }

    @Test
    public void isBetweenInclusiveDateEqualEnd() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = null;
        LocalDate end = new LocalDate(2019, 5, 8);

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertTrue(result);
    }

    @Test
    public void isBetweenInclusiveDateBeforeEnd() {
        LocalDate date = new LocalDate(2019, 5, 8);
        LocalDate start = null;
        LocalDate end = new LocalDate(2019, 5, 9);

        boolean result = DateHelper.isBetweenInclusive(date, start, end);

        assertTrue(result);
    }

}
