package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class KoskiYhdyshenkiloCopierTest {

    private DetailCopier copier;

    @Before
    public void setup() {
        copier = new KoskiYhdyshenkiloCopier();
    }

    @Test
    public void isMissingReturnFalseWhenKoskiYhdyshenkiloSet() {
        SearchResultRowDto dto = new SearchResultRowDto();
        dto.setKoskiYhdyshenkilo("example@example.com");

        assertThat(copier.isMissing(dto)).isFalse();
    }

    @Test
    public void isMissingReturnTrueWhenKoskiYhdyshenkiloNull() {
        SearchResultRowDto dto = new SearchResultRowDto();
        dto.setKoskiYhdyshenkilo(null);

        assertThat(copier.isMissing(dto)).isTrue();
    }

    @Test
    public void copyWithNullYhteystietoArvos() {
        OrganisaatioDetailsDto from = new OrganisaatioDetailsDto();
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getKoskiYhdyshenkilo);
    }

    @Test
    public void copyWithNullYhteystietoArvo() {
        OrganisaatioDetailsDto from = new OrganisaatioDetailsDto();
        from.setYhteystietoArvos(singletonList(new OrganisaatioYhteystietoElementtiDto()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getKoskiYhdyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiAndYhteystietoSuomiSetValue() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_1#1");
        from.setYhteystietoArvos(singletonList(yhteystieto("kieli_fi#1", "example@example.com")));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns("example@example.com", SearchResultRowDto::getKoskiYhdyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiAndYhteystietoRuotsiSetNull() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_1#1");
        from.setYhteystietoArvos(singletonList(yhteystieto("kieli_sv#1", "example@example.com")));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getKoskiYhdyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiRuotsiAndYhteystietoSuomiRuotsiSetFirstValue() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_3#1");
        from.setYhteystietoArvos(Stream.of(
                yhteystieto("kieli_fi#1", "fi@example.com"),
                yhteystieto("kieli_sv#1", "sv@example.com"))
                .collect(toList()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns("fi@example.com", SearchResultRowDto::getKoskiYhdyshenkilo);
    }

    private OrganisaatioDetailsDto organisaatio(String... opetuskielet) {
        OrganisaatioDetailsDto organisaatio = new OrganisaatioDetailsDto();
        organisaatio.setKieletUris(asList(opetuskielet));
        return organisaatio;
    }

    private OrganisaatioYhteystietoElementtiDto yhteystieto(String kieliUri, String arvo) {
        OrganisaatioYhteystietoElementtiDto yhteystieto = new OrganisaatioYhteystietoElementtiDto();
        yhteystieto.setTyyppiNimi("KOSKI-palvelun omien tietojen virheilmoituksen sähköpostiosoite");
        yhteystieto.setArvo(arvo);
        yhteystieto.setKieli(kieliUri);
        return yhteystieto;
    }

}
