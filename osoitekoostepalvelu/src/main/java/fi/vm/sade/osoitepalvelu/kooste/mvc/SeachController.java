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
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.mvc.dto.FilteredSearchParametersDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.*;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultPresentationByAddressFieldsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import fi.vm.sade.security.SimpleCache;
import org.apache.http.HttpStatus;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsView;

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
@Api(value="search",
    description = "Tarjoaa rajapinnan osoitteiden hakuun ja osoitteiden lataamiseen Excel-muodossa.")
@Controller
@Scope(value =  WebApplicationContext.SCOPE_APPLICATION)
@RequestMapping(value  =  "/search")
public class SeachController extends AbstractMvcController implements Serializable {

    private static final long serialVersionUID  =  -4986869170151316267L;

    @Autowired @Qualifier("actual")
    private SearchService searchService;

    @Autowired
    private SearchExcelService searchExcelService;

    @Autowired
    private SearchResultTransformerService resultTransformerService;

    private AtomicInteger i  =  new AtomicInteger(0);

    private static final int STORAGE_SIZE = 2048;
    private Map<String, FilteredSearchParametersDto> storedParameters  =  SimpleCache.buildCache(STORAGE_SIZE);

    /**
     * @param searchParameters to use
     * @param lang
     * @return the results
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForOrganisaatiosException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForHenkilosException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForKoulutusException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.OrganisaatioTyyppiMissingForOrganisaatiosException
     */
    @ApiOperation(value = "Palauttaa osoitteet annetuilla hakuehdoilla.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "list.json", method  =  RequestMethod.POST)
    @ResponseBody
    public SearchResultsPresentationDto list(@RequestBody FilteredSearchParametersDto searchParameters,
                            @RequestParam("lang") String lang)
            throws TooFewSearchConditionsForOrganisaatiosException,
            TooFewSearchConditionsForHenkilosException,
            TooFewSearchConditionsForKoulutusException,
            OrganisaatioTyyppiMissingForOrganisaatiosException {
        CamelRequestContext context  =  new DefaultCamelRequestContext();
        searchParameters.getSearchTerms().setLocale(parseLocale(lang));
        SearchResultsDto results = searchService.find(searchParameters.getSearchTerms(), context);
        SearchResultPresentation presentation = new SearchResultPresentationByAddressFieldsDto(
                searchParameters.getSearchTerms(),
                searchParameters.getNonIncludedOrganisaatioOids());
        return resultTransformerService.transformToResultRows(results, presentation, context, searchParameters.getSearchTerms().getSearchType());
    }

    /**
     * Prepares an Excel presentation by storing search parameters from a POST request.
     *
     * @param searchParameters to store
     * @return the key associated with the stored parameters to be used in the GET request for the Excel.
     */
    @ApiOperation("Palauttaa downloadId-tunnisteen, jolla voi excel.do-operaatiota käyttäen pyyttää GET-pyyntönä" +
            "Excel-tiedoston. Näin siksi, että hakuparametrit voitaisiin välitää URL-rajoitteen vuoksi POST-pyyntönä" +
            "mutta itse tiedostoon voidaan käyttöliittymässä tehdä uudelleenohjaus.")
    @RequestMapping(value = "prepare.excel.do", method  =  RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String storeExcelParameters(@RequestBody FilteredSearchParametersDto searchParameters) {
        String key  =  (i.incrementAndGet() + "." + new DateTime().toDate().getTime());
        String storeKey  =  resultTransformerService.getLoggeInUserOid() + "@" + key;
        this.storedParameters.put(storeKey, searchParameters);
        return key;
    }

    /**
     * Produces the excel for previously stored search parameters. A downloadId may only be used once and
     * only by the same user that has stored the parameters for that id. Results for any given downloadId may be
     * removed if downloadId has not been used instantly after storeExcelParameters call.
     *
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForOrganisaatiosException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForHenkilosException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.TooFewSearchConditionsForKoulutusException
     * @throws fi.vm.sade.osoitepalvelu.kooste.service.search.OrganisaatioTyyppiMissingForOrganisaatiosException
     * @see #storeExcelParameters(fi.vm.sade.osoitepalvelu.kooste.mvc.dto.FilteredSearchParametersDto)
     * There might be a better way around. Done this way so that we can avoid too long GET-request and redirect
     * browser to the download action without the need to store data on disk/db.
     *
     * @param downlaodId id returned form a prepare.excel.do call (associated with stored parameters)
     * @param lang the language to use
     * @return the Excel presentation
     * @throws NotFoundException if downloadId did not exist or already used.
     */
    @ApiOperation("Palauttaa Excel-tiedoston hakutuloksista aiemman prepare.excel.do-operaatiokutsun palauttaman" +
            " tunnisteen perusteella.")
    @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Hakua ei löytynyt downlaodId:llä.")
    @RequestMapping(value = "excel.do", method  =  RequestMethod.GET)
    public View downloadExcel(@RequestParam("downloadId") String downlaodId,
                   @RequestParam("lang") String lang)
            throws NotFoundException, TooFewSearchConditionsForOrganisaatiosException,
                    TooFewSearchConditionsForHenkilosException,
                    TooFewSearchConditionsForKoulutusException,
                    OrganisaatioTyyppiMissingForOrganisaatiosException {
        String storeKey  =  resultTransformerService.getLoggeInUserOid() + "@" + downlaodId;
        FilteredSearchParametersDto searchParameters  =  storedParameters.get(storeKey);
        if(searchParameters  == null) {
            throw new NotFoundException("Excel not found for download with key = " + downlaodId);
        }
        this.storedParameters.remove(storeKey); // <- not really REST here :/
        searchParameters.getSearchTerms().setLocale(parseLocale(lang));
        CamelRequestContext context = new DefaultCamelRequestContext();
        SearchResultsDto results = searchService.find(searchParameters.getSearchTerms(), context);
        SearchResultPresentation presentation  =  new SearchResultPresentationByAddressFieldsDto(
                searchParameters.getSearchTerms(),
                searchParameters.getNonIncludedOrganisaatioOids());
        final SearchResultsPresentationDto searchResults  =  resultTransformerService
                .transformToResultRows(results, presentation, context, searchParameters.getSearchTerms().getSearchType());
        return new AbstractXlsView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                              HttpServletResponse response) {
                response.setHeader("Content-Disposition", "attachment;filename=\"osoitteet.xls\"");
                searchExcelService.produceExcel(workbook, searchResults);
            }
        };
    }
}
