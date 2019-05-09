package fi.vm.sade.osoitepalvelu.kooste.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.oiva.KoulutuslupaDto;

import java.util.Collection;

public interface OivaRoute {

    Collection<KoulutuslupaDto> listKoulutuslupa(CamelRequestContext requestContext);

}
