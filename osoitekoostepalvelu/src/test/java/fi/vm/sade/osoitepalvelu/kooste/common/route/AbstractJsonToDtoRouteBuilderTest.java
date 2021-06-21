package fi.vm.sade.osoitepalvelu.kooste.common.route;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

public class AbstractJsonToDtoRouteBuilderTest {

    private AbstractJsonToDtoRouteBuilder builder;

    @Before
    public void setup() {
        builder = spy(AbstractJsonToDtoRouteBuilder.class);
    }

    @Test
    public void uriHttp4Component() {
        String url = "http://virkailija.opintopolku.fi?target=http://opintopolku.fi";

        String uri = builder.uri(url);

        assertThat(uri, startsWith("http4://virkailija.opintopolku.fi?target=http://opintopolku.fi"));
    }

    @Test
    public void uriHttps4Component() {
        String url = "https://virkailija.opintopolku.fi?target=http://opintopolku.fi";

        String uri = builder.uri(url);

        assertThat(uri, startsWith("https4://virkailija.opintopolku.fi?target=http://opintopolku.fi"));
    }

    @Test
    public void uriFtpComponent() {
        String url = "ftp://virkailija.opintopolku.fi?target=http://opintopolku.fi";

        String uri = builder.uri(url);

        assertThat(uri, startsWith("ftp://virkailija.opintopolku.fi?target=http://opintopolku.fi"));
    }

    @Test
    public void hasOpintopolkuHeaders() {
        AbstractJsonToDtoRouteBuilder.HeaderBuilder headerBuilder = builder.headers();
        assertTrue("Caller-Id header missing", headerBuilder.headers.containsKey("Caller-Id"));
        assertTrue("CSRF header missing", headerBuilder.headers.containsKey("CSRF"));
    }
}
