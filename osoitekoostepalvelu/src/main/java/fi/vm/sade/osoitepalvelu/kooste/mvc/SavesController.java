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

import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.SavedSearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchEditDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchListDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchSaveDto;
import fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.SavedSearchViewDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.Serializable;
import java.util.List;

@Api(value="saves", description = "Tarjoaa REST:n mukaiset CRUD-operaatiot käyttäjän omille " +
        "tallennetuille hauille.")
@Controller
@RequestMapping(value  =  "/saves")
public class SavesController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID  =  3284395720660368511L;
    
    @Autowired
    private SavedSearchService savedSearchService;

    @ApiOperation("Palauttaa kaikki käyttäjän omat haut pelkistettynä nimi-id-listana.")
    @RequestMapping(method  =  RequestMethod.GET)
    @ResponseBody
    public List<SavedSearchListDto> list() {
        return savedSearchService.findSavedSearchesForLoggedInUser();
    }

    @ApiOperation("Palauttaa yksittäisen, käyttäjän oman haun kaikki tiedot.")
    @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Hakua ei löytynyt id:llä.")
    @RequestMapping(value  =  "{id}", method  =  RequestMethod.GET)
    @ResponseBody
    public SavedSearchViewDto get(@PathVariable("id") long id) throws NotFoundException {
        return savedSearchService.getSaveById(id);
    }

    @ApiOperation("Poistaa yksittäisen käyttäjän oman tallennetun haun.")
    @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Hakua ei löytynyt id:llä.")
//    @ResponseStatus(value = HttpStatus.SC_OK)
    @RequestMapping(value  =  "{id}", method  =  RequestMethod.DELETE)
    @ResponseBody
    public int delete(@PathVariable("id") long id) throws NotFoundException {
        savedSearchService.deleteSavedSearch(id);
        return HttpStatus.SC_OK;
    }

    @ApiOperation("Tallentaa uuden haun.")
    @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Hakua ei löytynyt id:llä.")
    @RequestMapping(method  =  RequestMethod.POST)
    @ResponseBody
    public long save(@RequestBody SavedSearchSaveDto dto) throws NotFoundException {
        return savedSearchService.saveSearch(dto);
    }

    @ApiOperation("Päivittää käyttäjän oman tallennetun haun tiedot.")
    @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Hakua ei löytynyt id:llä.")
    @RequestMapping(method  =  RequestMethod.PUT)
    @ResponseBody
    public int edit(@RequestBody SavedSearchEditDto dto) throws NotFoundException {
        savedSearchService.updateSavedSearch(dto);
        return HttpStatus.SC_OK;
    }
}
