package fi.vm.sade.osoitepalvelu.kooste.service.kooste.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Expression;

/**
 * Helpperi, jonka avulla voidaan koostaa sopivia Camel-reittien headereita.
 */
public class HeaderBuilder {
    private Map<String, Expression> headers = new HashMap<String, Expression>();

    public HeaderBuilder() {
    }

    public HeaderBuilder(String headerName, Expression expr) {
        add(headerName, expr);
    }

    public HeaderBuilder add(String headerName, Expression expr) {
        headers.put(headerName, expr);
        return this;
    }

    public Expression getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    public Map<String, Expression> getHeaders() {
        return headers;
    }
}
