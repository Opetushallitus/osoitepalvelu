package fi.vm.sade.osoitepalvelu.service.mock;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloListResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.HenkiloService;

import java.util.List;

public class HenkiloServiceMock implements HenkiloService {
    private HenkiloDetailsDto henkiloDetailsDto;
    private List<HenkiloListResultDto> henkiloListResultDtos;
    private List<OrganisaatioHenkiloDto> organisaatiot;

    @Override
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return henkiloListResultDtos;
    }

    @Override
    public HenkiloDetailsDto getHenkiloTiedot(String oid, CamelRequestContext requestContext) {
        return henkiloDetailsDto;
    }

    @Override
    public List<OrganisaatioHenkiloDto> getOrganisaatiot(String oid, CamelRequestContext requestContext) {
        return organisaatiot;
    }

    public void setHenkiloTiedot(HenkiloDetailsDto henkiloTiedot) {
        this.henkiloDetailsDto = henkiloTiedot;
    }

    public void setHenkiloListResultDtos(List<HenkiloListResultDto> henkiloListResultDtos) {
        this.henkiloListResultDtos = henkiloListResultDtos;
    }

    public void setOrganisaatiot(List<OrganisaatioHenkiloDto> organisaatiot) {
        this.organisaatiot = organisaatiot;
    }
}
