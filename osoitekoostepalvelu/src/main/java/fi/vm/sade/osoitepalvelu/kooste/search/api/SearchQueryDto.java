package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchQueryDto implements Serializable {

    private static final long serialVersionUID = 1078154058705560606L;
    
    private Map<String, ConditionDto> taiEhdot;
    
    private Map<String, ConditionDto> jaEhdot;
    
    public SearchQueryDto() {
        taiEhdot = new HashMap<String, ConditionDto>();
        jaEhdot = new HashMap<String, ConditionDto>();
    }
    
    
    public Map<String, ConditionDto> getTaiEhdot() {
        return taiEhdot;
    }
    
    public Map<String, ConditionDto> getJaEhdot() {
        return jaEhdot;
    }
    
    public void addAndCriteria(SearchKeyDto keyDto, SearchValueDto value) {
        
        if( !jaEhdot.containsKey(keyDto.getKoodistoUri()) ) {
            ConditionDto dto = new ConditionDto();
            dto.setOperaattori(keyDto.getOperaattori());
            jaEhdot.put(keyDto.getKoodistoUri(), dto);
        }
        
        jaEhdot.get(keyDto.getKoodistoUri()).addEhto(value);
    }
    
    public void addOrCriteria(SearchKeyDto keyDto, SearchValueDto value) {
        
        if( !taiEhdot.containsKey(keyDto.getKoodistoUri()) ) {
            ConditionDto dto = new ConditionDto();
            dto.setOperaattori(keyDto.getOperaattori());
            taiEhdot.put(keyDto.getKoodistoUri(), dto);
        }
        
        taiEhdot.get(keyDto.getKoodistoUri()).addEhto(value);
    }
    
}
