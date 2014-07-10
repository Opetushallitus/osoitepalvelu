/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 12/17/13
 * Time: 8:58 AM
 */
@Document(collection  =  "koodistoCache")
public class KoodistoCache implements Serializable {
    private static final long serialVersionUID  =  4351568551821745238L;

    public enum KoodistoTyyppi {
            OPPILAITOSTYYPPI,
            OMISTAJATYYPPI,
            VUOSILUOKAT,
            MAAKUNTA,
            KUNTA,
            POSTINUMERO,
            TUTKINTOTYYPPI,
            TUTKINTO,
            KOULUTUS,
            KOULUTUSTYYPPI,
            OPPILAITOKSEN_OPETUSKIELI,
            KOULUTUS_KIELIVALIKOIMA,
            KOULUTUSASTEKELA,
            KOULUTUSTOIMIJA,
            KOULUTUSALAOPH2002,
            OPINTOALAOPH2002,
            ALUEHALLINTOVIRASTO,
            KAYTTOOIKEUSRYHMA,
            TUTKINTOTOIMIKUNTA,
            TUTKINTOTOIMIKUNTA_ROOLIS;
    }

    public static class CacheKey implements Serializable {
        private static final int HASH_FACTOR = 31;

        private static final long serialVersionUID  =  -1966991269006762979L;
        
        private KoodistoTyyppi tyyppi;
        private Locale locale;
        private String parentUri;

        public CacheKey() {
        }

        public CacheKey(KoodistoTyyppi tyyppi, Locale locale) {
            this.tyyppi  =  tyyppi;
            this.locale  =  locale;
        }

        public CacheKey(String parentUri, Locale locale) {
            this.parentUri  =  parentUri;
            this.locale  =  locale;
        }

        public KoodistoTyyppi getTyyppi() {
            return tyyppi;
        }

        public void setTyyppi(KoodistoTyyppi tyyppi) {
            this.tyyppi  =  tyyppi;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale  =  locale;
        }

        public String getParentUri() {
            return parentUri;
        }

        public void setParentUri(String parentUri) {
            this.parentUri  =  parentUri;
        }

        @Override
        public String toString() {
            StringBuffer b  =  new StringBuffer();
            if (tyyppi != null) {
                b.append("KoodistoTyyppi: ").append(tyyppi.name());
            }
            if (parentUri != null) {
                if (b.length() > 0) {
                    b.append(", ");
                }
                b.append("ParentURI: ").append(parentUri);
            }
            b.append(", Locale: ").append(locale);
            return b.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CacheKey cacheKey  =  (CacheKey) o;

            if (!locale.equals(cacheKey.locale)) {
                return false;
            }
            if (parentUri != null ? !parentUri.equals(cacheKey.parentUri) : cacheKey.parentUri != null) {
                return false;
            }
            if (tyyppi != cacheKey.tyyppi) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result  =  tyyppi != null ? tyyppi.hashCode() : 0;
            result  =  HASH_FACTOR * result + locale.hashCode();
            result  =  HASH_FACTOR * result + (parentUri != null ? parentUri.hashCode() : 0);
            return result;
        }
    }

    @Id
    private CacheKey key;
    private DateTime updatedAt  =  new DateTime();
    private List<KoodiItem> items  =  new ArrayList<KoodiItem>();

    public CacheKey getKey() {
        return key;
    }

    public void setKey(CacheKey key) {
        this.key  =  key;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt  =  updatedAt;
    }

    public List<KoodiItem> getItems() {
        return items;
    }

    public void setItems(List<KoodiItem> items) {
        this.items  =  items;
    }
}
