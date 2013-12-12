package fi.vm.sade.osoitepalvelu.kooste.dao.save;

import fi.vm.sade.osoitepalvelu.kooste.dao.sequence.SequenceRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/11/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DefaultSaveSearchRepository extends SimpleMongoRepository<SavedSearch, Long> implements SavedSearchRepository {
    @Autowired
    private SequenceRepository sequenceRepository;

    public DefaultSaveSearchRepository(MongoEntityInformation<SavedSearch, Long> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultSaveSearchRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<SavedSearch, Long>getEntityInformation(SavedSearch.class), mongoOperations);
    }

    @Override
    public List<SavedSearch> findByOwnerUsername(String ownerUsername, Sort order) {
        Criteria criteria = new Criteria().where("ownerUsername").is(ownerUsername);
        return getMongoOperations().find(Query.query(criteria).with(order), SavedSearch.class);
    }

    @Override
    public SavedSearch saveNew(SavedSearch savedSearch) {
        savedSearch.setId(sequenceRepository.getNextSavedSearchIdSequence());
        return super.save(savedSearch);
    }
}
