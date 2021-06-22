package fi.vm.sade.osoitepalvelu.kooste.common.route.cas;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultCookieBasedCasTicketCacheTest {

    private static final String SERVICE = "test";
    private static final String CAS_COOKIE = "TGC=TEST";
    private static final String CSRF_COOKIE = "CSRF=CSRF";

    private DefaultCookieBasedCasTicketCache cache;

    @Before
    public void setup() {
        cache = new DefaultCookieBasedCasTicketCache();
    }

    @Test
    public void happyPath() {
        Map<String, Object> headers = Map.of(DefaultCookieBasedCasTicketCache.SET_COOKIE_HEADER, CAS_COOKIE);
        cache.store(SERVICE, headers);
        assertEquals("Unexpected cookie value", String.join(";", Arrays.asList(CAS_COOKIE, CSRF_COOKIE)), cache.get(SERVICE).get(DefaultCookieBasedCasTicketCache.COOKIE_HEADER));
    }

    @Test
    public void noCookies() {
        cache.store(SERVICE, Collections.EMPTY_MAP);
        assertNull("Cookies should not be null", cache.get(SERVICE));
    }

    @Test
    public void multipleCookies() {
        Map<String, Object> headers = Map.of(DefaultCookieBasedCasTicketCache.SET_COOKIE_HEADER, Arrays.asList(CAS_COOKIE, CSRF_COOKIE));
        cache.store(SERVICE, headers);
        assertNotNull("Cookies should not be null", cache.get(SERVICE).get(DefaultCookieBasedCasTicketCache.COOKIE_HEADER));
        assertEquals("Unexpected cookie value", String.join(";", Arrays.asList(CAS_COOKIE, CSRF_COOKIE)), cache.get(SERVICE).get(DefaultCookieBasedCasTicketCache.COOKIE_HEADER));
    }
}
