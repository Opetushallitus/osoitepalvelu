package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
@DtoConversion
public class SearchTermDto implements Serializable {
    private String type;
    private List<String> values = new ArrayList<String>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
