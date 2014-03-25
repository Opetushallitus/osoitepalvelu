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

package fi.vm.sade.osoitepalvelu.kooste.mvc;

import com.wordnik.swagger.annotations.Api;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.SavedSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:32 PM
 */
@Api("Tallennetut haut")
@Controller
@RequestMapping(value  =  "/saves")
public class SavesController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID  =  3284395720660368511L;
    
    @Autowired
    private SavedSearchService savedSearchService;

    @RequestMapping(method  =  RequestMethod.GET)
    @ResponseBody
    public List<SavedSearchListDto> list() {
        return savedSearchService.findSavedSearchesForLoggedInUser();
    }

    @RequestMapping(value  =  "{id}", method  =  RequestMethod.GET)
    @ResponseBody
    public SavedSearchViewDto get(@PathVariable("id") long id) throws NotFoundException {
        return savedSearchService.getSaveById(id);
    }

    @RequestMapping(value  =  "{id}", method  =  RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable("id") long id) throws NotFoundException {
        savedSearchService.deleteSavedSearch(id);
        return "OK";
    }

    @RequestMapping(method  =  RequestMethod.PUT)
    @ResponseBody
    public long save(@RequestBody SavedSearchSaveDto dto) throws NotFoundException {
        return savedSearchService.saveSearch(dto);
    }

    @RequestMapping(method  =  RequestMethod.POST)
    @ResponseBody
    public String edit(@RequestBody SavedSearchEditDto dto) throws NotFoundException {
        savedSearchService.updateSavedSearch(dto);
        return "OK";
    }
}
