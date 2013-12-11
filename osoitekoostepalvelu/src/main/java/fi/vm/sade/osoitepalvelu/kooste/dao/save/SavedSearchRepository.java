package fi.vm.sade.osoitepalvelu.kooste.dao.save;

import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SavedSearchRepository extends MongoRepository<SavedSearch, Long> {

    public List<SavedSearch> findByOwnerUsername(String ownerUsername, Sort order);

}
