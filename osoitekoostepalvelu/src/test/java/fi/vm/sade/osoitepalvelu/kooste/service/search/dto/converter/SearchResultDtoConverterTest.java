package fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import org.hamcrest.CoreMatchers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

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
                    String koodi = invocation.getArgument(1);
                    UiKoodiItemDto uiKoodiItemDto = new UiKoodiItemDto();
                    uiKoodiItemDto.setKoodiUri(koodi);
                    uiKoodiItemDto.setNimi("nimi/" + koodi);
                    return uiKoodiItemDto;
                });
        OrganisaatioYhteystietoHakuResultDto hakuResultDto = new OrganisaatioYhteystietoHakuResultDto();
        hakuResultDto.setKielet(new HashSet<>(Arrays.asList("suomi", "ruotsi")));

        OrganisaatioResultDto resultDto = converter.convert(hakuResultDto, new OrganisaatioResultDto(), Locale.ITALY);

        assertThat(resultDto.getKieletUris(), CoreMatchers.hasItems("suomi", "ruotsi"));
        assertEquals("nimi/suomi,nimi/ruotsi", resultDto.getOpetuskieli());
        verify(koodistoService).findOppilaitoksenOpetuskieliByKoodiUri(eq(Locale.ITALY), eq("suomi"));
        verify(koodistoService).findOppilaitoksenOpetuskieliByKoodiUri(eq(Locale.ITALY), eq("ruotsi"));
        verifyNoMoreInteractions(koodistoService);
    }

}
