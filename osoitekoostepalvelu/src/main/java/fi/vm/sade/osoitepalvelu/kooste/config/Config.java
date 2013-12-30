package fi.vm.sade.osoitepalvelu.kooste.config;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/30/13
 * Time: 9:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    private int cacheTimeoutMillis;

    public int getCacheTimeoutMillis() {
        return cacheTimeoutMillis;
    }

    public void setCacheTimeoutMillis(int cacheTimeoutMillis) {
        this.cacheTimeoutMillis = cacheTimeoutMillis;
    }
}
