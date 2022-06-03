package fi.vm.sade.osoitepalvelu.kooste.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OpetuskieliHelperTest {

    private final String code;

    public OpetuskieliHelperTest(String code) {
        this.code = code;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"oppilaitoksenopetuskieli_1#1"},
                {"oppilaitoksenopetuskieli_1#2"},
                {"oppilaitoksenopetuskieli_1#3"},
                {"oppilaitoksenopetuskieli_1"},
                {"oppilaitoksenopetuskieli_1#foobar"},
        });
    }

    @Test
    public void convertCodeValueToLang() {
        assertTrue(OpetuskieliHelper.opetuskieliToKielet(code).contains("kieli_fi"));
    }
}
