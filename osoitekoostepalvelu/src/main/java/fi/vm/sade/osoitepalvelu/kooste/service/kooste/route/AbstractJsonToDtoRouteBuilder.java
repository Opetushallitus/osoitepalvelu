package fi.vm.sade.osoitepalvelu.kooste.service.kooste.route;

import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;

/**
 * Abstrakti kantaluokka, joka tarjoaa peruspalvelut Camel-reittien luomiseen,
 * joilla voi lukea HTTP GET pyynnöllä JSON datan halutusta URL:sta ja
 * automaattisesti konvertoida datan tietyksi DTO-luokiksi.
 * 
 * Tämä luokka on toteutettu helpottamaan ja nopeuttamaan JSON Camel-reittien
 * rakentamista osoitepalvelussa.
 */
public abstract class AbstractJsonToDtoRouteBuilder extends SpringRouteBuilder {

	@Autowired
	protected ObjectMapperProvider mapperProvider;
	
	protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, TypeReference<T> targetDtoType) {
		JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
		return from(routeId)					
					.setHeader(Exchange.HTTP_METHOD, constant("GET"))						
						.to(url)							
							.process(jsonToDtoConverter);
	}
	
	protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, String headerName, Expression headerValue, TypeReference<T> targetDtoType) {
		JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
		return from(routeId)					
					.setHeader(Exchange.HTTP_METHOD, constant("GET"))
					.setHeader(headerName, headerValue)
						.to(url)							
							.process(jsonToDtoConverter);	
	}
	
	protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, HeaderBuilder headers, TypeReference<T> targetDtoType) {
		JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);		
		RouteDefinition route = from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET"));
		// Asetetaan tässä käyttäjän antamat headereiden arvot
		for (Entry<String, Expression> header : headers.getHeaders().entrySet()) {
			route.setHeader(header.getKey(), header.getValue());
		}
		return route.to(url).process(jsonToDtoConverter);
	}
	
	protected LoadBalanceDefinition addRouteErrorHandlers(RouteDefinition route) {
		boolean roundRobin = true;
		boolean inheritErrorHandler = true;		
		return route.loadBalance().failover(10, inheritErrorHandler, roundRobin);
	}

	protected ProducerTemplate getCamelTemplate() {
		return this.getApplicationContext().getBean(ProducerTemplate.class);
	}
	
	protected String trim(String value) {
		if (value != null) {
			return value.trim();
		}
		return null;
	}
}
