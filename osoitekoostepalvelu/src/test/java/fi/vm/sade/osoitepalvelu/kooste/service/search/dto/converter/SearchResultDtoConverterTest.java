package fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchResultDtoConverterTest {

    @InjectMocks
    private SearchResultDtoConverter converter;

    @Mock
    private KoodistoService koodistoService;

    @Test
    public void opetuskieli() {
        when(koodistoService.findOppilaitoksenOpetuskieliByKoodiUri(any(), any()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    String koodi = invocation.getArgumentAt(1, String.class);
                    UiKoodiItemDto uiKoodiItemDto = new UiKoodiItemDto();
                    uiKoodiItemDto.setKoodiUri(koodi);
                    uiKoodiItemDto.setNimi("nimi/" + koodi);
                    return uiKoodiItemDto;
                });
        OrganisaatioYhteystietoHakuResultDto hakuResultDto = new OrganisaatioYhteystietoHakuResultDto();
        hakuResultDto.setKielet(Arrays.asList("suomi", "ruotsi"));

        OrganisaatioResultDto resultDto = converter.convert(hakuResultDto, new OrganisaatioResultDto(), Locale.ITALY);

        assertThat(resultDto.getKieletUris(), CoreMatchers.hasItems("suomi", "ruotsi"));
        assertEquals("nimi/suomi,nimi/ruotsi", resultDto.getOpetuskieli());
        verify(koodistoService).findOppilaitoksenOpetuskieliByKoodiUri(eq(Locale.ITALY), eq("suomi"));
        verify(koodistoService).findOppilaitoksenOpetuskieliByKoodiUri(eq(Locale.ITALY), eq("ruotsi"));
        verifyNoMoreInteractions(koodistoService);
    }

    @Test
    public void testConvert() {

        List<OrganisaatioYhteystietoHakuResultDto> fixture = Arrays.asList(
                new OrganisaatioYhteystietoHakuResultDto()
        );

        List<OrganisaatioResultDto> results = converter.convert(
                fixture, new ArrayList<>(),
                OrganisaatioResultDto.class, Locale.ITALY);

        assertEquals(1, results.size());
        assertTrue(results.get(0).getKayntiosoite().isEmpty());
    }

    @Test
    public void testConvertWithKayntiosoite() {

        OrganisaatioYhteystietoHakuResultDto dto = new OrganisaatioYhteystietoHakuResultDto();
        dto.getKayntiosoite().add(new OrganisaatioYhteysosoiteDto());

        List<OrganisaatioYhteystietoHakuResultDto> fixture = Arrays.asList(dto);

        List<OrganisaatioResultDto> results = converter.convert(
                fixture, new ArrayList<>(),
                OrganisaatioResultDto.class, Locale.ITALY);

        assertEquals(1, results.size());
        assertFalse(results.get(0).getKayntiosoite().isEmpty());
    }
}
