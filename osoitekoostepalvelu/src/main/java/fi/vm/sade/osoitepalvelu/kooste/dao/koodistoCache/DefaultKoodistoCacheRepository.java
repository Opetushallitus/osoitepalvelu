package fi.vm.sade.osoitepalvelu.kooste.dao.koodistoCache;

import fi.vm.sade.osoitepalvelu.kooste.dao.sequence.SequenceRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.KoodistoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/17/13
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DefaultKoodistoCacheRepository extends SimpleMongoRepository<KoodistoCache, KoodistoCache.CacheKey>
        implements KoodistoCacheRepository {
    @Autowired
    private SequenceRepository sequenceRepository;

    public DefaultKoodistoCacheRepository(MongoEntityInformation<KoodistoCache, KoodistoCache.CacheKey> metadata,
            MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultKoodistoCacheRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<KoodistoCache, KoodistoCache.CacheKey>getEntityInformation(KoodistoCache.class), mongoOperations);
    }

    @Override
    public KoodistoCache findCacheByTypeAndLocale(KoodistoCache.KoodistoTyyppi tyyppi, Locale locale) {
        return findOne(new KoodistoCache.CacheKey(tyyppi, locale));
    }
}
