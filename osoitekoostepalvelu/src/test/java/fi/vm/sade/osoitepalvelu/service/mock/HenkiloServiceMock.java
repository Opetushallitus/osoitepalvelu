package fi.vm.sade.osoitepalvelu.service.mock;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.HenkiloService;

import java.util.List;

public class HenkiloServiceMock implements HenkiloService {

    private List<HenkiloDetailsDto> henkiloDetailsDtos;

    @Override
    public List<HenkiloDetailsDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return henkiloDetailsDtos;
    }

    public void setHenkiloDetailsDtos(List<HenkiloDetailsDto> henkiloDetailsDtos) {
        this.henkiloDetailsDtos = henkiloDetailsDtos;
    }
}
