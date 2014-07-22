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

package fi.vm.sade.osoitepalvelu.kooste.service.saves;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:45 PM
 */
public interface SavedSearchService extends Serializable {

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
