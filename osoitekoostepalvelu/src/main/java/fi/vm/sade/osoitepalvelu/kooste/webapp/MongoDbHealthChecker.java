package fi.vm.sade.osoitepalvelu.kooste.webapp;

import fi.vm.sade.generic.healthcheck.HealthChecker;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.apache.camel.util.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 3:43 PM
 */
@Component
@HealthCheckerName("database")
public class MongoDbHealthChecker implements HealthChecker {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Object checkHealth() throws Throwable {
        final StopWatch watch = new StopWatch();
        final long number = mongoTemplate.count(Query.query(new Criteria()), SavedSearch.class);
        final long resultTook = watch.stop();
        return new LinkedHashMap(){{
            put("status", "OK");
            put("response-time", ""+resultTook+" ms");
            put("number-of-saved-searches", number);
        }};
    }
}
