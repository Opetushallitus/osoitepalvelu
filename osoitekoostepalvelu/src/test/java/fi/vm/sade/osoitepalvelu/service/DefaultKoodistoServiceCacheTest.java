package fi.vm.sade.osoitepalvelu.service;

import fi.vm.sade.osoitepalvelu.AbstractMongoAwareTest;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.route.KoodistoReitti;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/30/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultKoodistoServiceCacheTest extends AbstractMongoAwareTest {
    private static final Locale LOCALE_FI = new Locale("fi", "FI");

    @Autowired
    private DefaultKoodistoService koodistoService;

    @Autowired
    private KoodistoReitti koodistoReitti;

    @Test
    public void testCache() {
        long defaultCache = koodistoService.getCacheTimeoutMillis(),
                orignalSearchCount = koodistoReitti.getFindCounterValue();
        koodistoReitti.setFindCounterUsed(true);

        // Ensure cache turned off:
        koodistoService.setCacheTimeoutMillis(-1l);

        List<UiKoodiItemDto> optiot = koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
        DefaultKoodistoServiceTest.assertListNonEmptyAndItemsOfType(optiot, KoodistoDto.KoodistoTyyppi.ALUEHALLINTOVIRASTO);

        // Ensure cache not used:
        assertEquals( orignalSearchCount+1l, koodistoReitti.getFindCounterValue() );

        // Turn cache on:
        koodistoService.setCacheTimeoutMillis(1000l*3600l);

        List<UiKoodiItemDto> optiot2 = koodistoService.findAlueHallintoVirastoOptions(LOCALE_FI);
        // Ensure cache not used:
        assertEquals(orignalSearchCount + 1l, koodistoReitti.getFindCounterValue());

        // And that results match the original:
        DefaultKoodistoServiceTest.assertListNonEmptyAndItemsOfType(optiot2, KoodistoDto.KoodistoTyyppi.ALUEHALLINTOVIRASTO);
        assertEquals( optiot.size(), optiot2.size() );

        koodistoService.setCacheTimeoutMillis(defaultCache);
    }
}
