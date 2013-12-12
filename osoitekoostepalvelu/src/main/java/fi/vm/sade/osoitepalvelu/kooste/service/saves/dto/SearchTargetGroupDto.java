package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.ratamaa.dtoconverter.annotation.DtoConverted;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
@DtoConversion
public class SearchTargetGroupDto implements Serializable {
    private SearchTargetGroup.GroupType type;
    private List<SearchTargetGroup.TargetType> options = new ArrayList<SearchTargetGroup.TargetType>();

    public SearchTargetGroup.GroupType getType() {
        return type;
    }

    public void setType(SearchTargetGroup.GroupType type) {
        this.type = type;
    }

    public List<SearchTargetGroup.TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<SearchTargetGroup.TargetType> options) {
        this.options = options;
    }
}
