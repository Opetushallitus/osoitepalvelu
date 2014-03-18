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
import fi.vm.sade.osoitepalvelu.kooste.mvc.dto.FilteredSearchParametersDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultTransformerService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultPresentationByAddressFieldsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.security.SimpleCache;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 2:45 PM
 */
@Api("Haku")
@Controller
@Scope(value= WebApplicationContext.SCOPE_APPLICATION)
@RequestMapping(value = "/search")
public class SeachController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID = -4986869170151316267L;

    @Autowired @Qualifier("dummy")
    private SearchService searchService;

    @Autowired
    private SearchResultTransformerService resultTransformerService;

    private AtomicInteger i = new AtomicInteger(0);
    private Map<String, FilteredSearchParametersDto> storedParameters = SimpleCache.buildCache(2048);

    /**
     * @param searchParameters to use
     * @return the results
     */
    @RequestMapping(value="list.json", method = RequestMethod.POST)
    public @ResponseBody SearchResultsDto list( @RequestBody FilteredSearchParametersDto searchParameters,
                                                @RequestParam("lang") String lang ) {
        OrganisaatioResultsDto results = searchService.find(searchParameters.getSearchTerms());
        SearchResultPresentation presentation = new SearchResultPresentationByAddressFieldsDto(
                searchParameters.getSearchTerms(), parseLocale(lang),
                searchParameters.getNonIncludedOrganisaatioOids() );
        return resultTransformerService.transformToResultRows(results.getResults(), presentation);
    }

    /**
     * Prepares an Excel presentation by storing search parameters from a POST request.
     *
     * @param searchParameters to store
     * @return the key associated with the stored parameters to be used in the GET request for the Excel.
     */
    @RequestMapping(value="prepare.excel.do", method = RequestMethod.POST)
    public @ResponseBody String storeExcelParameters( @RequestBody FilteredSearchParametersDto searchParameters  ) {
        String key = (i.incrementAndGet()+"."+new DateTime().toDate().getTime());
        String storeKey = resultTransformerService.getLoggeInUserOid()+"@"+key;
        this.storedParameters.put(storeKey, searchParameters);
        return key;
    }

    /**
     * Produces the excel for previously stored search parameters. A downloadId may only be used once and
     * only by the same user that has stored the parameters for that id. Results for any given downloadId may be
     * removed if downloadId has not been used instantly after storeExcelParameters call.
     *
     * @see #storeExcelParameters(fi.vm.sade.osoitepalvelu.kooste.mvc.dto.FilteredSearchParametersDto)
     * There might be a better way around. Done this way so that we can avoid too long GET-request and redirect
     * borwser to the download action without the need to store data on disk/db.
     *
     * @param downlaodId id returned form a prepare.excel.do call (associated with stored parameters)
     * @param lang the language to use
     * @return the Excel presentation
     * @throws NotFoundException if downloadId did not exist or already used.
     */
    @RequestMapping(value="excel.do", method = RequestMethod.GET)
    public View downloadExcel( @RequestParam("downloadId") String downlaodId,
                               @RequestParam("lang") String lang ) throws NotFoundException {
        String storeKey = resultTransformerService.getLoggeInUserOid()+"@"+downlaodId;
        FilteredSearchParametersDto searchParameters = storedParameters.get(storeKey);
        if( searchParameters == null ) {
            throw new NotFoundException("Excel not found for download with key="+downlaodId);
        }
        this.storedParameters.remove(storeKey); // <- not really REST here :/
        OrganisaatioResultsDto results = searchService.find(searchParameters.getSearchTerms());
        SearchResultPresentation presentation = new SearchResultPresentationByAddressFieldsDto(
                searchParameters.getSearchTerms(), parseLocale(lang),
                searchParameters.getNonIncludedOrganisaatioOids() );
        final SearchResultsDto searchResults = resultTransformerService.transformToResultRows(results.getResults(), presentation);
        return new AbstractExcelView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
                resultTransformerService.produceExcel(workbook, searchResults);
            }
        };
    }
}
