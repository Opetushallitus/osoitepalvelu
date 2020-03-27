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
import fi.vm.sade.auditlog.Changes;
import fi.vm.sade.auditlog.Target;
import fi.vm.sade.osoitepalvelu.kooste.OsoitepalveluOperation;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.dao.save.SavedSearchRepository;
import fi.vm.sade.osoitepalvelu.kooste.dao.sequence.SequenceRepository;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.AuditHelper.getUser;

@Service
public class DefaultSavedSearchService extends AbstractService implements SavedSearchService {
    private static final long serialVersionUID = -7200048189159586281L;

    @Autowired
    private SavedSearchDtoConverter dtoConverter;

    @Autowired(required = false)
    private SavedSearchRepository savedSearchRepository;

    @Autowired(required = false)
    private SequenceRepository sequenceRepository;

    @Autowired
    private Audit audit;

    @Override
    public List<SavedSearchListDto> findSavedSearchesForLoggedInUser() {
        return dtoConverter.convert(savedSearchRepository.findByOwnerUsername(getLoggedInUserOid()),
                        new ArrayList<SavedSearchListDto>(), SavedSearchListDto.class);
    }

    @Override
    public SavedSearchViewDto getSaveById(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save = savedSearchRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found by primary key."));
        ensureLoggedInUser(save.getOwnerUserOid());
        return dtoConverter.convert(save, new SavedSearchViewDto());
    }

    @Override
    public void deleteSavedSearch(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save = savedSearchRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found by primary key."));
        ensureLoggedInUser(save.getOwnerUserOid());
        savedSearchRepository.deleteById(save.getId());
        Target target = new Target.Builder().setField("id", String.valueOf(id)).build();
        Changes changes = new Changes.Builder().build();
        audit.log(getUser(), OsoitepalveluOperation.DELETE_SAVE, target, changes);
    }

    @Override
    public long saveSearch(SavedSearchSaveDto dto) {
        SavedSearch search  =  dtoConverter.convert(dto, new SavedSearch());
        search.setOwnerUserOid(getLoggedInUserOid());
        Long id = sequenceRepository.getNextSavedSearchIdSequence();
        search.setId(id);
        savedSearchRepository.save(search);
        Target target = new Target.Builder().setField("id", String.valueOf(id)).build();
        Changes changes = new Changes.Builder().build();
        audit.log(getUser(), OsoitepalveluOperation.NEW_SAVE, target, changes);
        return id;
    }

    @Override
    public void updateSavedSearch(SavedSearchEditDto dto) throws NotFoundException, AuthorizationException {
        SavedSearch save = savedSearchRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("Entity not found by primary key."));
        dtoConverter.convert(dto, save);
        savedSearchRepository.save(save);
        Target target = new Target.Builder().setField("id", String.valueOf(dto.getId())).build();
        Changes changes = new Changes.Builder().build();
        audit.log(getUser(), OsoitepalveluOperation.UPDATE_SAVE, target, changes);
    }
}
