package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class OrganisaatioDtoConverterTest {

    private OrganisaatioDtoConverter converter = new OrganisaatioDtoConverter();

    @Test
    public void kieletUris() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setKieletUris(asList("suomi", "ruotsi"));

        OrganisaatioYhteystietoHakuResultDto hakuResultDto = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertThat(hakuResultDto.getKielet(), CoreMatchers.hasItems("suomi", "ruotsi"));
    }

    @Test
    public void extractsPostiosoiteCorrectly() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setYhteystiedot(Arrays.asList(
                createYhteystieto(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.posti.name(), "test"),
                createYhteystieto("foo", "bar")
        ));

        OrganisaatioYhteystietoHakuResultDto result = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertEquals(1, result.getPostiosoite().size());
        assertEquals("test", result.getPostiosoite().get(0).getOsoite());
    }

    @Test
    public void extractsKayntiosoiteCorrectly() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setYhteystiedot(Arrays.asList(
                createYhteystieto(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.kaynti.name(), "test"),
                createYhteystieto("foo", "bar")
        ));

        OrganisaatioYhteystietoHakuResultDto result = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertEquals(1, result.getKayntiosoite().size());
        assertEquals("test", result.getKayntiosoite().get(0).getOsoite());
    }

    private OrganisaatioDetailsYhteystietoDto createYhteystieto(String tyyppi, String osoite) {
        OrganisaatioDetailsYhteystietoDto yhteystieto = new OrganisaatioDetailsYhteystietoDto();
        yhteystieto.setOsoiteTyyppi(tyyppi);
        yhteystieto.setOsoite(osoite);
        return yhteystieto;
    }

    @Test
    public void resolveKriisitiedoituksenEmailFound() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setYhteystietoArvos(Arrays.asList(
                createYhteystietoArvo("tyyppi", "Kriisitiedotuksen sähköpostiosoite", "foo@bar.qux")
        ));

        OrganisaatioYhteystietoHakuResultDto result = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertEquals("foo@bar.qux", result.getKriisitiedotuksenEmail());
    }

    @Test
    public void resolveKriisitiedoituksenEmailNotFound() {
        OrganisaatioDetails details = new OrganisaatioDetails();
        details.setYhteystietoArvos(Arrays.asList(
                createYhteystietoArvo(null, "tyyppiNimi", null),
                createYhteystietoArvo("tyyppi", "tyyppiNimi", null),
                createYhteystietoArvo("tyyppi", "Kriisitiedotuksen sähköpostiosoite", null)
        ));

        OrganisaatioYhteystietoHakuResultDto result = converter.convert(details, new OrganisaatioYhteystietoHakuResultDto());

        assertNull(result.getKriisitiedotuksenEmail());
    }

    private OrganisaatioYhteystietoElementtiDto createYhteystietoArvo(String tyyppi, String tyyppiNimi, String arvo) {
        OrganisaatioYhteystietoElementtiDto details = new OrganisaatioYhteystietoElementtiDto();
        details.setElementtiTyyppi(tyyppi);
        details.setTyyppiNimi(tyyppiNimi);
        details.setArvo(arvo);
        return details;
    }
}
