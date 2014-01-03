package fi.vm.sade.osoitepalvelu.kooste.dao.koodistoCache;

import fi.vm.sade.osoitepalvelu.kooste.domain.KoodistoCache;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/17/13
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KoodistoCacheRepository extends MongoRepository<KoodistoCache, KoodistoCache.CacheKey> {

    KoodistoCache findCacheByTypeAndLocale(KoodistoCache.KoodistoTyyppi tyyppi, Locale locale);

}
