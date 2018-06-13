package fi.vm.sade.osoitepalvelu.kooste.service.search.detailcopier;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class KayntiosoiteCopier implements DetailCopier {

    @Autowired
    private KoodistoService koodistoService;

    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Override
    public boolean isMissing(SearchResultRowDto from) {
        return from.getKayntiosoite() == null;
    }

    @Override
    public void copy(OrganisaatioDetailsDto from, SearchResultRowDto to, Locale locale) {
        to.setKayntiosoite(dtoConverter.convert(from.getKayntiosoite(), new SearchResultOsoiteDto(), locale));
        if (to.getKayntiosoite() != null && from.getKayntiosoite() != null
                && from.getKayntiosoite().getPostinumeroUri() != null) {
            UiKoodiItemDto postinumeroKoodi  =  koodistoService
                    .findPostinumeroByKoodiUri(locale, from.getKayntiosoite().getPostinumeroUri());
            if (postinumeroKoodi != null) {
                to.getKayntiosoite().setPostinumero(postinumeroKoodi.getKoodiId());
                to.getKayntiosoite().setPostitoimipaikka(postinumeroKoodi.getNimi());
            }
        }
    }
}
