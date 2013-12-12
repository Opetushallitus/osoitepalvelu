package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConverted;
import fi.ratamaa.dtoconverter.annotation.DtoNotExported;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
@DtoConverted
public class SavedSearchEditDto {
    @DtoNotExported
    private Long id;
    private SaveType type;
    private List<String> addressFields = new ArrayList<String>();
    private List<SearchTargetGroupDto> targetGroups = new ArrayList<SearchTargetGroupDto>();
    private List<SearchTermDto> terms = new ArrayList<SearchTermDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaveType getType() {
        return type;
    }

    public void setType(SaveType type) {
        this.type = type;
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
