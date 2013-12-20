package fi.vm.sade.osoitepalvelu.search.api;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioSearchKeyDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioSearchQueryDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioSearchValueDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;

public class ApiToJsonTest {

    @Test
    public void generateSearchJsonExample() {

        OrganisaatioSearchQueryDto haku = new OrganisaatioSearchQueryDto();

        haku.addOrCriteria(new OrganisaatioSearchKeyDto(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI.toString(), "TAI"),
                new OrganisaatioSearchValueDto("1"));
        haku.addOrCriteria(new OrganisaatioSearchKeyDto(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI.toString(), "TAI"),
                new OrganisaatioSearchValueDto("2"));

        haku.addAndCriteria(new OrganisaatioSearchKeyDto(KoodistoTyyppi.MAAKUNTA.toString(), "JA"),
                new OrganisaatioSearchValueDto("06"));

        ObjectMapper mapper = new ObjectMapper();
        
        mapper.getSerializationConfig().without(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        if( !mapper.canSerialize(OrganisaatioSearchQueryDto.class) ) {
            System.out.println("VIRHE!");
        }

        
            try {
                System.out.println(mapper.writeValueAsString(haku));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

}
