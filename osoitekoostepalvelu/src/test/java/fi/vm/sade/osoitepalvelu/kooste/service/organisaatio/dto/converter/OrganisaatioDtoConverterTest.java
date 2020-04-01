package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.domain.Organisaatio;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class OrganisaatioDtoConverterTest {

    private OrganisaatioDtoConverter converter;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void setup() {
        converter = new OrganisaatioDtoConverter();
    }

    @Test
    public void kieletUris() {
        Organisaatio details = new Organisaatio();
        details.setKieletUris(new HashSet<>(asList("suomi", "ruotsi")));
        logger.info("detaissia: " + details);
        OrganisaatioYhteystietoHakuResultDto hakuResultDto = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());
        assertThat(hakuResultDto.getKielet(), CoreMatchers.hasItems("suomi", "ruotsi"));
    }

}
