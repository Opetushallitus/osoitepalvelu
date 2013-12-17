package fi.vm.sade.osoitepalvelu.kooste.service.saves;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.dao.save.SavedSearchRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.converter.SavedSearchDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DefaultSavedSearchService extends AbstractService implements SavedSearchService {

    @Autowired
    private SavedSearchDtoConverter dtoConverter;

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SavedSearchListDto> findSavedSearchesForLoggedInUser() {
        return dtoConverter.convert(savedSearchRepository.findByOwnerUsername(getLoggedInUserOid(),
                        new Sort(Sort.Direction.ASC, "name")),
                new ArrayList<SavedSearchListDto>(), SavedSearchListDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SavedSearchViewDto getSaveById(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save = found(savedSearchRepository.findOne(id));
        ensureLoggedInUser(save.getOwnerUserOid());
        return dtoConverter.convert(save, new SavedSearchViewDto());
    }

    @Override
    @Transactional
    public void deleteSavedSearch(long id) throws NotFoundException, AuthorizationException {
        SavedSearch save = found(savedSearchRepository.findOne(id));
        ensureLoggedInUser(save.getOwnerUserOid());
        savedSearchRepository.delete(id);
    }

    @Override
    @Transactional
    public long saveSearch(SavedSearchSaveDto dto) {
        SavedSearch search = dtoConverter.convert(dto, new SavedSearch());
        search.setOwnerUserOid(getLoggedInUserOid());
        return savedSearchRepository.saveNew(search).getId();
    }

    @Override
    @Transactional
    public void updateSavedSearch(SavedSearchEditDto dto) throws NotFoundException, AuthorizationException {
        SavedSearch save = found(savedSearchRepository.findOne(dto.getId()));
        dtoConverter.convert(dto, save);
        savedSearchRepository.save(save);
    }
}
