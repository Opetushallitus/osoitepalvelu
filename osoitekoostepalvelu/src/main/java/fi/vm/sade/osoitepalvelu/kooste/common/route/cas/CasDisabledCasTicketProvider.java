package fi.vm.sade.osoitepalvelu.kooste.common.route.cas;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tommiratamaa on 15.4.2014.
 */
public class CasDisabledCasTicketProvider implements CasTicketProvider {

    @Override
    public Map<String, Object> provideTicketHeaders(String service) {
        return new HashMap<String, Object>();
    }
}
