package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class OrganisaatioDtoConverterTest {

    private OrganisaatioDtoConverter converter;

    @Before
    public void setup() {
        converter = new OrganisaatioDtoConverter();
    }

    @Test
    public void kieletUris() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setKieletUris(asList("suomi", "ruotsi"));

        OrganisaatioYhteystietoHakuResultDto hakuResultDto = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertThat(hakuResultDto.getKielet(), CoreMatchers.hasItems("suomi", "ruotsi"));
    }

}
