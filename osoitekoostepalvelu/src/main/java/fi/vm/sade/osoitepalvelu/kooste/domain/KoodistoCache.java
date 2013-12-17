package fi.vm.sade.osoitepalvelu.kooste.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/17/13
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "koodistoCache")
public class KoodistoCache implements Serializable {

    public enum KoodistoTyyppi {
            OPPILAITOSTYYPPI,
            OMISTAJATYYPPI,
            VUOSILUOKAT,
            MAAKUNTA,
            KUNTA,
            TUTKINTOTYYPPI,
            TUTKINTO,
            OPPILAITOKSEN_OPETUSKIELI,
            KOULUTUS_KIELIVALIKOIMA,
            KOULUTUSASTEKELA,
            KOULUTUSTOIMIJA,
            OPINTOALAOPH2002,
            ALUEHALLINTOVIRASTO;
    }

    public static class CacheKey implements Serializable {
        private KoodistoTyyppi tyyppi;
        private Locale locale;

        public CacheKey() {
        }

        public CacheKey(KoodistoTyyppi tyyppi, Locale locale) {
            this.tyyppi = tyyppi;
            this.locale = locale;
        }

        public KoodistoTyyppi getTyyppi() {
            return tyyppi;
        }

        public void setTyyppi(KoodistoTyyppi tyyppi) {
            this.tyyppi = tyyppi;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }

    @Id
    private CacheKey key;
    private DateTime updatedAt = new DateTime();
    private List<KoodiItem> items = new ArrayList<KoodiItem>();

    public CacheKey getKey() {
        return key;
    }

    public void setKey(CacheKey key) {
        this.key = key;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<KoodiItem> getItems() {
        return items;
    }

    public void setItems(List<KoodiItem> items) {
        this.items = items;
    }
}
