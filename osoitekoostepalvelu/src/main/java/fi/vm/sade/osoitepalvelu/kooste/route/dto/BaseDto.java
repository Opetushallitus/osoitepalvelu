package fi.vm.sade.osoitepalvelu.kooste.route.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseDto {

    private final Map<String, Object> others = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getOthers() {
        return others;
    }

    @JsonAnySetter
    public void putOther(String name, Object value) {
        others.put(name, value);
    }

}
