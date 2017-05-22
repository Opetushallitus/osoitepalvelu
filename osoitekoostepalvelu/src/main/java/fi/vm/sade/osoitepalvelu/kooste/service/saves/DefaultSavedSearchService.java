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

import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.auditlog.osoitepalvelu.OsoitepalveluOperation;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.dao.save.SavedSearchRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.converter.SavedSearchDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import fi.vm.sade.auditlog.osoitepalvelu.LogMessage;
import static fi.vm.sade.auditlog.osoitepalvelu.LogMessage.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:09 PM
 */
@Service
public class DefaultSavedSearchService extends AbstractService implements SavedSearchService {
    private static final long serialVersionUID = -7200048189159586281L;

    @Autowired
    private SavedSearchDtoConverter dtoConverter;

    @Autowired(required = false)
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private Audit audit;

    @Override
    public List<SavedSearchListDto> findSavedSearchesForLoggedInUser() {
        return dtoConverter.convert(savedSearchRepository.findByOwnerUsername(getLoggedInUserOid(),
                        new Sort(Sort.Direction.ASC, "name")),
                        new ArrayList<SavedSearchListDto>(), SavedSearchListDto.class);
    }

    @Override
    public SavedSearchViewDto getSaveById(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save  =  found(savedSearchRepository.findOne(id));
        ensureLoggedInUser(save.getOwnerUserOid());
        return dtoConverter.convert(save, new SavedSearchViewDto());
    }

    @Override
    public void deleteSavedSearch(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save  =  found(savedSearchRepository.findOne(id));
        ensureLoggedInUser(save.getOwnerUserOid());
        savedSearchRepository.delete(id);
        LogMessage logMessage = builder().id(getLoggedInUserOid()).setOperaatio(OsoitepalveluOperation.DELETE_SAVE)
                .build();
        audit.log(logMessage);
    }

    @Override
    public long saveSearch(SavedSearchSaveDto dto) {
        SavedSearch search  =  dtoConverter.convert(dto, new SavedSearch());
        search.setOwnerUserOid(getLoggedInUserOid());
        LogMessage logMessage = builder().id(getLoggedInUserOid()).setOperaatio(OsoitepalveluOperation.NEW_SAVE)
                .build();
        audit.log(logMessage);
        return savedSearchRepository.saveNew(search).getId();
    }

    @Override
    public void updateSavedSearch(SavedSearchEditDto dto) throws NotFoundException, AuthorizationException {
        SavedSearch save  =  found(savedSearchRepository.findOne(dto.getId()));
        dtoConverter.convert(dto, save);
        savedSearchRepository.save(save);
        LogMessage logMessage = builder().id(getLoggedInUserOid()).setOperaatio(OsoitepalveluOperation.UPDATE_SAVE)
                .build();
        audit.log(logMessage);
    }

    protected <T> T found(T obj) throws NotFoundException {
        if (obj == null) {
            throw new NotFoundException("Entity not found by primary key.");
        }
        return obj;
    }
}
