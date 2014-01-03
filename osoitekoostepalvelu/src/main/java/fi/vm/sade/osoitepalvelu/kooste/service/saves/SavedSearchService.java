package fi.vm.sade.osoitepalvelu.kooste.service.saves;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SavedSearchService {

    /**
     * @return all saved searches for the logged in user for listing
     */
    List<SavedSearchListDto> findSavedSearchesForLoggedInUser();

    /**
     * @param id of the saved search to return
     * @return the saved search with full details
     * @throws NotFoundException if search was not found by given id
     * @throws AuthorizationException if user does not own the saved search in question
     */
    SavedSearchViewDto getSaveById(long id) throws NotFoundException, AuthorizationException;

    /**
     * Deletes the given saved search owned by the logged in user
     *
     * @param id of the saved search to delete
     * @throws NotFoundException if not found by id
     * @throws AuthorizationException if user does not own the saved search in question
     */
    void deleteSavedSearch(long id) throws NotFoundException, AuthorizationException;

    /**
     * @param dto to be saved
     * @return the id of the saved entity
     */
    long saveSearch(SavedSearchSaveDto dto);

    /**
     * @param dto to be updated
     * @throws NotFoundException if the saved search was not found by provided id
     * @throws AuthorizationException if logged in user does not own the saved search in question
     */
    void updateSavedSearch(SavedSearchEditDto dto) throws NotFoundException, AuthorizationException;
}
