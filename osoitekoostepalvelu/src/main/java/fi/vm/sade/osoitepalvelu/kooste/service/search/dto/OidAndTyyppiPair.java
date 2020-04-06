package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 1:49 PM
 */
@ApiModel("Kuvaa organisaation/henkilön hakutuloksissa. Jos oid ei ole tiedossa, voidaan käyttää rivinumeroa.")
public class OidAndTyyppiPair implements Serializable {
    private static final long serialVersionUID = 639768390250422033L;
    
    private static final int HASH_FACTOR = 31;
    public static final String TYYPPI_ORGANISAATIO  =  "organisaatio";
    public static final String TYYPPI_HENKILO  =  "henkilo";

    @ApiModelProperty("Oid-tyyppi: organisaatio|henkilo")
    private String oidTyyppi;
    @ApiModelProperty("OID:n arvo.")
    private String oid;
    @ApiModelProperty("Rivinumero hakutuloksissa. Tätä verrataan, jos oid on null.")
    private int rivinumero;

    public OidAndTyyppiPair() {
    }

    public OidAndTyyppiPair(String oidTyyppi, String oid, int rivinumero) {
        this.oidTyyppi = oidTyyppi;
        this.oid = oid;
        this.rivinumero = rivinumero;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid  =  oid;
    }

    public String getOidTyyppi() {
        return oidTyyppi;
    }

    public void setOidTyyppi(String oidTyyppi) {
        this.oidTyyppi  =  oidTyyppi;
    }

    public int getRivinumero() {
        return rivinumero;
    }

    public void setRivinumero(int rivinumero) {
        this.rivinumero = rivinumero;
    }

    @Override
    public int hashCode() {
        int result = oid != null ? oid.hashCode() : 0;
        result = HASH_FACTOR * result  +  (oidTyyppi != null ? oidTyyppi.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  OidAndTyyppiPair)) {
            return false;
        }
        OidAndTyyppiPair other  =  (OidAndTyyppiPair) obj;
        return EqualsHelper.areEquals(this.oid, other.oid)
                && EqualsHelper.areEquals(this.oidTyyppi, other.oidTyyppi)
                && this.rivinumero == other.rivinumero;
    }
}
