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

import fi.vm.sade.osoitepalvelu.kooste.config.swagger.SwaggerTwoConfig;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

// import fi.vm.sade.osoitepalvelu.kooste.config.swagger.CustomSwaggerConfig;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.service.ApiInfo;


@Configuration
@ComponentScan(basePackages  =  {
    "fi.vm.sade.osoitepalvelu.kooste.mvc",
    "fi.vm.sade.osoitepalvelu.kooste.scheduled"
})
@ImportResource("classpath:spring/spring-mvc.xml")
@Import(SwaggerTwoConfig.class) // Swagger
public class SpringMvcApp {
    /*
    private SpringSwaggerConfig springSwaggerConfig;
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation(){
        RelativeSwaggerPathProvider path = new RelativeSwaggerPathProvider();
        TypeResolver resolver = new TypeResolver();
        path.setApiResourcePrefix("osoitekoostepalvelu/api");
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

    }
    */
}
