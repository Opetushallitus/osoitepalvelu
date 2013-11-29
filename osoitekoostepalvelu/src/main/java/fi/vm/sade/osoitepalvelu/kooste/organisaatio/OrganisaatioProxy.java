package fi.vm.sade.osoitepalvelu.kooste.organisaatio;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;

/**
 * 
 * @author Jussi Jartamo
 * 
 *         Proxy organisaation kutsumiseen, seamless retry 10 kertaa!
 */
public interface OrganisaatioProxy {

    OrganisaatioRDTO haeOrganisaatio(String tarjoajaOid);
}
