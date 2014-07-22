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

package fi.vm.sade.osoitepalvelu.service.saves;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.DefaultSavedSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.converter.SavedSearchDtoConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 12/30/13
 * Time: 10:32 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestAppConfig.class)
public class DefaultSavedSearchServiceTest {

    @Autowired
    private DefaultSavedSearchService savedSearchService;

    @Autowired
    private SavedSearchDtoConverter dtoConverter;

    private List<Long> cleanupSaveIds;

    @Before
    public void init() {
        loginAs("testUser");
        cleanupSaveIds  =  new ArrayList<Long>();
    }

    @After
    public void cleanup() throws NotFoundException {
        loginAs("testUser");
        for (Long id : cleanupSaveIds) {
            savedSearchService.deleteSavedSearch(id);
        }
    }

    private void loginAs(String user) {
        SecurityContextHolder.getContext().setAuthentication(new RunAsUserToken("1234" + user, user, "1234",
                Collections.<GrantedAuthority>emptyList(), null));
    }

    @Test
    public void testSaveSearch() {
        SavedSearchSaveDto dto  =  createSavedSearch("Test", SavedSearch.SaveType.CONTACT);
        long id  =  savedSearchService.saveSearch(dto);
        cleanupSaveIds.add(id);
        assertTrue(id > 0L);
    }

    @Test
    public void testGetSavedSearch() throws NotFoundException {
        long saveId  =  savedSearchService.saveSearch(createSavedSearch("Test save", SavedSearch.SaveType.EMAIL));
        cleanupSaveIds.add(saveId);
        SavedSearchViewDto dto  =  savedSearchService.getSaveById(saveId);
        assertEquals(saveId, dto.getId().longValue());
        assertEquals(SavedSearch.SaveType.EMAIL, dto.getSearchType());
        assertEquals(1, dto.getAddressFields().size());
        assertEquals(1, dto.getTargetGroups().size());
        assertEquals(2, dto.getTerms().size());
    }

    @Test(expected  =  NotFoundException.class)
    public void testDeleteSavedSearch() throws NotFoundException {
        long saveId  =  savedSearchService.saveSearch(createSavedSearch("Test save", SavedSearch.SaveType.EMAIL));
        try {
            savedSearchService.deleteSavedSearch(saveId);
        } catch (NotFoundException e) {
            throw new IllegalStateException(e);
        }
        savedSearchService.getSaveById(saveId);
    }

    @Test(expected  =  AuthorizationException.class)
    public void testDeleteWhenNotOwner() throws NotFoundException {
        long saveId  =  savedSearchService.saveSearch(createSavedSearch("Test save", SavedSearch.SaveType.EMAIL));
        cleanupSaveIds.add(saveId);
        loginAs("otherUser");
        savedSearchService.deleteSavedSearch(saveId);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        SavedSearchSaveDto dto  =  createSavedSearch("Test", SavedSearch.SaveType.CONTACT);
        long id  =  savedSearchService.saveSearch(dto);
        cleanupSaveIds.add(id);

        SavedSearchEditDto editDto  =  dtoConverter.convert(dto, new SavedSearchEditDto());
        editDto.setId(id);
        editDto.setSearchType(SavedSearch.SaveType.LETTER);
        editDto.setAddressFields(Arrays.asList(new String[]{"TestField", "TestField2"}));
        savedSearchService.updateSavedSearch(editDto);

        SavedSearchViewDto viewDto  =  savedSearchService.getSaveById(id);
        assertEquals(SavedSearch.SaveType.LETTER, viewDto.getSearchType());
        assertEquals(2, viewDto.getAddressFields().size());
        assertEquals(1, viewDto.getTargetGroups().size());
        assertEquals(2, viewDto.getTerms().size());
    }

    @Test(expected  =  AuthorizationException.class)
    public void testUpdateWhenNotOwner() throws NotFoundException {
        long saveId  =  savedSearchService.saveSearch(createSavedSearch("Test save", SavedSearch.SaveType.EMAIL));
        cleanupSaveIds.add(saveId);
        loginAs("otherUser");
        SavedSearchEditDto editDto  =  dtoConverter.convert(savedSearchService.getSaveById(saveId), new SavedSearchEditDto());
        savedSearchService.updateSavedSearch(editDto);
    }

    @Test
    public void testFindSavedSearches() {
        cleanupSaveIds.add(savedSearchService.saveSearch(createSavedSearch("A", SavedSearch.SaveType.CONTACT)));
        cleanupSaveIds.add(savedSearchService.saveSearch(createSavedSearch("B", SavedSearch.SaveType.CONTACT)));

        List<SavedSearchListDto> dtos  =  savedSearchService.findSavedSearchesForLoggedInUser();
        assertEquals(2, dtos.size());
        assertEquals("A", dtos.get(0).getName());
        assertEquals("B", dtos.get(1).getName());
        assertTrue(!dtos.get(0).getId().equals(dtos.get(1).getId()));

        loginAs("otherUser");
        dtos  =  savedSearchService.findSavedSearchesForLoggedInUser();
        assertEquals(0, dtos.size());
    }

    private SavedSearchSaveDto createSavedSearch(String name, SavedSearch.SaveType saveType) {
        SavedSearchSaveDto dto  =  new SavedSearchSaveDto();
        dto.setName(name);
        dto.setSearchType(saveType);
        dto.setAddressFields(Arrays.asList(new String[]{"TestField"}));
        dto.setTargetGroups(Arrays.asList(new SearchTargetGroupDto[] {
                new SearchTargetGroupDto(SearchTargetGroup.GroupType.OPETUSPISTEET,
                    Arrays.asList(new SearchTargetGroup.TargetType[] {
                            SearchTargetGroup.TargetType.YHTEYSHENKILO
                    }))
        }));
        dto.setTerms(Arrays.asList(new SearchTermDto[] {
                new SearchTermDto("avis", Arrays.asList(new String[] {"testAvi"})),
                new SearchTermDto("maakuntas", Arrays.asList(new String[] {"ahvenanmaa", "pirkanmaa"}))
        }));
        return dto;
    }
}
