package fi.vm.sade.osoitepalvelu.service.mock;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloListResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.HenkiloService;

import java.util.List;

public class HenkiloServiceMock implements HenkiloService {

    private List<HenkiloListResultDto> henkiloListResultDtos;

    @Override
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return henkiloListResultDtos;
    }

    public void setHenkiloListResultDtos(List<HenkiloListResultDto> henkiloListResultDtos) {
        this.henkiloListResultDtos = henkiloListResultDtos;
    }
}
