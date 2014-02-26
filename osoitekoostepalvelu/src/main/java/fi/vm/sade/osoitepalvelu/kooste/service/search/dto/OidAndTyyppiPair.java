package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 1:49 PM
 */
public class OidAndTyyppiPair implements Serializable {
    public static final String TYYPPI_ORGANISAATIO = "organisaatio";
    public static final String TYYPPI_HENKILO = "henkilo";

    private String oidTyyppi;
    private String oid;

    public OidAndTyyppiPair() {
    }

    public OidAndTyyppiPair(String oidTyyppi, String oid) {
        this.oidTyyppi = oidTyyppi;
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOidTyyppi() {
        return oidTyyppi;
    }

    public void setOidTyyppi(String oidTyyppi) {
        this.oidTyyppi = oidTyyppi;
    }

    @Override
    public int hashCode() {
        int result = oid != null ? oid.hashCode() : 0;
        result = 31 * result + (oidTyyppi != null ? oidTyyppi.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof  OidAndTyyppiPair) ) {
            return false;
        }
        OidAndTyyppiPair other = (OidAndTyyppiPair) obj;
        return EqualsHelper.equals(this.oid, other.oid)
                && EqualsHelper.equals(this.oidTyyppi, other.oidTyyppi);
    }
}
