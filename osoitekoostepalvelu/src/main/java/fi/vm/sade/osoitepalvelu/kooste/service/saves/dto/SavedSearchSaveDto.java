package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoConverted;
import fi.ratamaa.dtoconverter.annotation.DtoExported;
import fi.ratamaa.dtoconverter.annotation.DtoNotImported;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
@DtoConversion
@DtoNotImported
public class SavedSearchSaveDto {
    private String name;
    private SavedSearch.SaveType searchType;
    private List<String> addressFields = new ArrayList<String>();
    private List<String> receiverFields = new ArrayList<String>();
    private List<SearchTargetGroupDto> targetGroups = new ArrayList<SearchTargetGroupDto>();
    private List<SearchTermDto> terms = new ArrayList<SearchTermDto>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SavedSearch.SaveType getSearchType() {
        return searchType;
    }

    public void setSearchType(SavedSearch.SaveType searchType) {
        this.searchType = searchType;
    }

    public List<String> getReceiverFields() {
        return receiverFields;
    }

    public void setReceiverFields(List<String> receiverFields) {
        this.receiverFields = receiverFields;
    }

    public List<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(List<String> addressFields) {
        this.addressFields = addressFields;
    }

    public List<SearchTargetGroupDto> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<SearchTargetGroupDto> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public List<SearchTermDto> getTerms() {
        return terms;
    }

    public void setTerms(List<SearchTermDto> terms) {
        this.terms = terms;
    }
}
