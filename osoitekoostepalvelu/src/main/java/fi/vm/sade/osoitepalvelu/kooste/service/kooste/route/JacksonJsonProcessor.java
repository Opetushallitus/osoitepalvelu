package fi.vm.sade.osoitepalvelu.kooste.service.kooste.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import fi.vm.sade.osoitepalvelu.kooste.config.ObjectMapperProvider;

/**
 * Camel-viestiketjun prosessori, jonka avulla voi helposti tehdä JSON konversion 
 * tavalliseksi (Pojo) Java -luokkasi. JSON -> Pojo mappauksen käytetään
 * Jackson kirjastoa, joka on jo valmiiksi konfiguroitu palveluun. 
 */
public class JacksonJsonProcessor implements Processor {	
	
	private ObjectMapperProvider mapperProvider;
	
	@SuppressWarnings("rawtypes")
	private TypeReference targetClassType;
	
	public <T> JacksonJsonProcessor(ObjectMapperProvider mapperProvider, TypeReference<T> targetClassType) {
		this.targetClassType = targetClassType;
		this.mapperProvider = mapperProvider;
	}
	
	@SuppressWarnings("rawtypes")
	public TypeReference getTargetClassType() {
		return targetClassType;
	}

	/**
	 * Metodi, joka lukee Camel -viestin merkkijonona ja konvertoi JSON datan
	 * TypeReference tyyppiseksi olioksi.
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		ObjectMapper objectMapper = mapperProvider.getContext(targetClassType.getClass());
		String jsonData = exchange.getIn().getBody(String.class);
		exchange.getOut().setBody(objectMapper.readValue(jsonData, targetClassType));
	}
}
