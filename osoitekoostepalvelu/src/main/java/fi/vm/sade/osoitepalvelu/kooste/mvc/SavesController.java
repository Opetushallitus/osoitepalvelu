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

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Api("Tallennetut haut")
@Controller
@RequestMapping(value = "/saves")
public class SavesController extends AbstractMvcController {
    @Autowired
    private SavedSearchService savedSearchService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<SavedSearchListDto> list() {
        return savedSearchService.findSavedSearchesForLoggedInUser();
    }

    @RequestMapping(value="{id}", method = RequestMethod.GET)
    public @ResponseBody SavedSearchViewDto get(@PathVariable("id") long id) throws NotFoundException {
        return savedSearchService.getSaveById(id);
    }

    @RequestMapping(value="{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable("id") long id) throws NotFoundException {
        savedSearchService.deleteSavedSearch(id);
        return "OK";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody long save( @RequestBody SavedSearchSaveDto dto ) throws NotFoundException {
        return savedSearchService.saveSearch(dto);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String edit( @RequestBody SavedSearchEditDto dto ) throws NotFoundException {
        savedSearchService.updateSavedSearch(dto);
        return "OK";
    }
}
