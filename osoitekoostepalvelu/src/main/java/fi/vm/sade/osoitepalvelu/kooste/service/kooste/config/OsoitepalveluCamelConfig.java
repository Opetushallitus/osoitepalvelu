package fi.vm.sade.osoitepalvelu.kooste.service.kooste.config;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Apache Camel ohjelmistokehyksen alustava luokka. Ajetaan, kun palvelu käynnistyy.
 */
@Configuration
public class OsoitepalveluCamelConfig {

    private ProducerTemplate producerTemplate;

    /**
     * Alustaa Camel-kontekstin ja lisää kaikki tunnetut reitit tähän
     * kontekstiin. Palvelussa täytyy olla ainakin yksi SpringRouteBuilder
     * -luokasta periytetty luokka, jotta palvelin ohjelma suostuu
     * käynnistymään.
     */
    @Bean
    public CamelContext getCamelContext(RoutesBuilder[] builders, ApplicationContext appContext) throws Exception {
        SpringCamelContext context = new SpringCamelContext(appContext);
        for (RoutesBuilder route : builders) {
            context.addRoutes(route);
        }
        context.setAutoStartup(true);
        return context;
    }

    /**
     * Camel ProducerTemplate luonti metodi. Luodaan yksi template yleisesti
     * osoitepalvelun käyttöön.
     * 
     * @param context
     *            Camel konteksti.
     * @return Viite Camel ProducerTemplate -luokkaan, jonka kautta voi välittää
     *         dataa Camel-reitteihin.
     */
    @Bean(name = "camelTemplate")
    public ProducerTemplate getProducerTemplate(CamelContext context) {
        if (producerTemplate == null) {
            producerTemplate = context.createProducerTemplate();
        }
        return producerTemplate;
    }
}
