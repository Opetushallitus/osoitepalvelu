package fi.vm.sade.osoitepalvelu.kooste.route;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.config.UrlConfiguration;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.oiva.KoulutuslupaDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class DefaultOivaRoute extends AbstractJsonToDtoRouteBuilder implements OivaRoute {

    private static final String KOULUTUSLUPA_LIST =  "direct:koulutuslupaList";

    private final UrlConfiguration urlConfiguration;

    public DefaultOivaRoute(UrlConfiguration urlConfiguration) {
        this.urlConfiguration = urlConfiguration;
    }

    @Override
    public void configure() throws Exception {
        buildKoulutuslupaList();
    }

    private void buildKoulutuslupaList() {
        headers(
                from(KOULUTUSLUPA_LIST),
                headers()
                        .basicAuth(urlConfiguration.require("oiva.username"), urlConfiguration.require("oiva.password"))
                        .get()
                        .retry(3)
        )
                .to(uri(urlConfiguration.getProperty("oiva.koulutusluvat")))
                .process(jsonToDto(new TypeReference<List<KoulutuslupaDto>>() {}));
    }

    @Override
    public Collection<KoulutuslupaDto> listKoulutuslupa(CamelRequestContext requestContext) {
        return sendBodyHeadersAndProperties(getCamelTemplate(), KOULUTUSLUPA_LIST,
                "", new HashMap<>(), requestContext, List.class);
    }

}
