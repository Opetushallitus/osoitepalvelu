package fi.vm.sade.osoitepalvelu.kooste.dao.sequence;

import fi.vm.sade.osoitepalvelu.kooste.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/12/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DefaultSequenceRepository implements SequenceRepository {
    private static final String SAVED_SEARCH_ID_SEQUENCE_NAME = "savedSearch";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getNextSavedSearchIdSequence() {
        return increaseCounter(SAVED_SEARCH_ID_SEQUENCE_NAME);
    }

    private long increaseCounter(String sequenceName){
        Query query = new Query(Criteria.where("name").is(sequenceName));
        Update update = new Update().inc("sequence", 1);
        Sequence seq = mongoTemplate.findAndModify(query, update, Sequence.class);
        if( seq == null ) {
            seq = new Sequence();
            seq.setName(sequenceName);
            seq.setSequence(1l);
            mongoTemplate.save(seq);
        }
        return seq.getSequence();
    }

}
