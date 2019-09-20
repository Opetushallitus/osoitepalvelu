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

public class MoveYhteyshenkiloCopierTest {

    private DetailCopier copier;

    @Before
    public void setup() {
        copier = new MoveYhteyshenkiloCopier();
    }

    @Test
    public void isMissingReturnFalseWhenMoveYhteyshenkiloSet() {
        SearchResultRowDto dto = new SearchResultRowDto();
        dto.setMoveYhteyshenkilo("move@example.com");

        assertThat(copier.isMissing(dto)).isFalse();
    }

    @Test
    public void isMissingReturnTrueWhenMoveYhteyshenkiloNull() {
        SearchResultRowDto dto = new SearchResultRowDto();
        dto.setMoveYhteyshenkilo(null);

        assertThat(copier.isMissing(dto)).isTrue();
    }

    @Test
    public void copyWithNullYhteystietoArvos() {
        OrganisaatioDetailsDto from = new OrganisaatioDetailsDto();
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getMoveYhteyshenkilo);
    }

    @Test
    public void copyWithNullYhteystietoArvo() {
        OrganisaatioDetailsDto from = new OrganisaatioDetailsDto();
        from.setYhteystietoArvos(singletonList(new OrganisaatioYhteystietoElementtiDto()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getMoveYhteyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiAndYhteystietoSuomiSetValue() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_1#1");
        from.setYhteystietoArvos(Stream.of(
                yhteystieto("ei-move-yhteystieto1", "kieli_fi#1", "ei-move@example.com"),
                yhteystieto("kieli_fi#1", "move@example.com"),
                yhteystieto("ei-move-yhteystieto2", "kieli_fi#1", "ei-move@example.com"))
                .collect(toList()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns("move@example.com", SearchResultRowDto::getMoveYhteyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliRuotsiAndYhteystietoRuotsiSetValue() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_2#1");
        from.setYhteystietoArvos(Stream.of(
                yhteystieto("ei-move-yhteystieto1", "kieli_sv#1", "ei-move@example.com"),
                yhteystieto("kieli_sv#1", "move@example.com"),
                yhteystieto("ei-move-yhteystieto2", "kieli_sv#1", "ei-move@example.com"))
                .collect(toList()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns("move@example.com", SearchResultRowDto::getMoveYhteyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiAndYhteystietoRuotsiSetNull() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_1#1");
        from.setYhteystietoArvos(singletonList(yhteystieto("kieli_sv#1", "move@example.com")));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns(null, SearchResultRowDto::getMoveYhteyshenkilo);
    }

    @Test
    public void copyWithOpetuskieliSuomiRuotsiAndYhteystietoSuomiRuotsiSetFirstValue() {
        OrganisaatioDetailsDto from = organisaatio("oppilaitoksenopetuskieli_3#1");
        from.setYhteystietoArvos(Stream.of(
                yhteystieto("kieli_sv#1", "sv@example.com"),
                yhteystieto("kieli_fi#1", "fi@example.com"))
                .collect(toList()));
        SearchResultRowDto to = new SearchResultRowDto();

        copier.copy(from, to, null);

        assertThat(to).returns("sv@example.com", SearchResultRowDto::getMoveYhteyshenkilo);
    }

    private OrganisaatioDetailsDto organisaatio(String... opetuskielet) {
        OrganisaatioDetailsDto organisaatio = new OrganisaatioDetailsDto();
        organisaatio.setKieletUris(asList(opetuskielet));
        return organisaatio;
    }

    private OrganisaatioYhteystietoElementtiDto yhteystieto(String kieliUri, String arvo) {
        return yhteystieto("MOVE!-yhteyshenkilö/mittaustulosten syöttäjä", kieliUri, arvo);
    }

    private OrganisaatioYhteystietoElementtiDto yhteystieto(String tyyppi, String kieliUri, String arvo) {
        OrganisaatioYhteystietoElementtiDto yhteystieto = new OrganisaatioYhteystietoElementtiDto();
        yhteystieto.setTyyppiNimi(tyyppi);
        yhteystieto.setArvo(arvo);
        yhteystieto.setKieli(kieliUri);
        return yhteystieto;
    }

}
