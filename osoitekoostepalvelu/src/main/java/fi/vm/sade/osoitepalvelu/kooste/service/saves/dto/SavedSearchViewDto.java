package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoNotExported;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@DtoConversion
@DtoNotExported
public class SavedSearchViewDto implements Serializable {
    private Long id;
    private SavedSearch.SaveType searchType;
    private DateTime createdAt;
    private List<String> addressFields = new ArrayList<String>();
    private List<String> receiverFields = new ArrayList<String>();
    private List<SearchTargetGroupDto> targetGroups = new ArrayList<SearchTargetGroupDto>();
    private List<SearchTermDto> terms = new ArrayList<SearchTermDto>();

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SavedSearch.SaveType getSearchType() {
        return searchType;
    }

    public void setSearchType(SavedSearch.SaveType searchType) {
        this.searchType = searchType;
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

    public List<String> getReceiverFields() {
        return receiverFields;
    }

    public void setReceiverFields(List<String> receiverFields) {
        this.receiverFields = receiverFields;
    }
}
