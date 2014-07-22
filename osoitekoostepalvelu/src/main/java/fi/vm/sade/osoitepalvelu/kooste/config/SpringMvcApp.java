/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.fasterxml.classmate.TypeResolver;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.alternates.AlternateTypeRule;
import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

import fi.vm.sade.osoitepalvelu.kooste.config.swagger.CustomSwaggerConfig;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:16 PM
 */
@Configuration
@ComponentScan(basePackages  =  {
    "fi.vm.sade.osoitepalvelu.kooste.mvc",
    "fi.vm.sade.osoitepalvelu.kooste.scheduled"
})
@ImportResource("classpath:spring/spring-mvc.xml")
@Import(CustomSwaggerConfig.class) // Swagger
public class SpringMvcApp {

    private SpringSwaggerConfig springSwaggerConfig;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation(){
        RelativeSwaggerPathProvider path = new RelativeSwaggerPathProvider();
        TypeResolver resolver = new TypeResolver();
        path.setApiResourcePrefix("api");
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .apiVersion("1.0")
                .pathProvider(path)
                .alternateTypeRules(
                    new AlternateTypeRule(resolver.resolve(BigDecimal.class), resolver.resolve(Double.class)),
                    new AlternateTypeRule(resolver.resolve(BigInteger.class), resolver.resolve(Long.class)),
                    new AlternateTypeRule(resolver.resolve(DateTime.class), resolver.resolve(Date.class)),
                    new AlternateTypeRule(resolver.resolve(LocalDate.class), resolver.resolve(Date.class))
                );
    }

    protected ApiInfo apiInfo() {
        return new ApiInfo(
                "Osoitepalvelu", /* title */
                "Osoitepalvelu on Opetushallituksen työntekijöille tarkoitettu osoitetietojen hakemista varten." +
                        "Palvelussa on käyttöliittymä, jonka avulla työntekijä voi hakea osoitetietoja eri" +
                        " hakukriteereillä. Hakukriteerit voi myös tallentaa haluamalleen nimelle myöhempää " +
                        "tarvetta varten. Tiedot koostetaan muista palveluista.",
                null, /* TOS URL */
                null, /* Contact */
                "EUPL", /* license */
                "http://www.osor.eu/eupl/" /* license URL */
        );
    }
}
