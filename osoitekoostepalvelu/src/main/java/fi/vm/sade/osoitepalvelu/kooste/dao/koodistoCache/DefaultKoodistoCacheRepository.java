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
 * User: ratamaa
 * Date: 12/17/13
 * Time: 9:11 AM
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
        KoodistoCache.CacheKey key = new KoodistoCache.CacheKey(tyyppi, locale);
        return findOne(key);
    }
}
