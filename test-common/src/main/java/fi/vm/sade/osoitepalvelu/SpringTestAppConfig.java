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

package fi.vm.sade.osoitepalvelu;

import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;

import fi.vm.sade.auditlog.ApplicationType;
import fi.vm.sade.auditlog.Audit;

/**
 * User: ratamaa
 * Date: 12/30/13
 * Time: 9:27 AM
 */
@Configuration
@ComponentScan(basePackages  =  {
        "fi.vm.sade.osoitepalvelu.kooste.common",
        "fi.vm.sade.osoitepalvelu.kooste.domain",
        "fi.vm.sade.osoitepalvelu.kooste.service"
})
@ImportResource("classpath:spring/test-application-context.xml")
@PropertySource({"classpath:/osoitekoostepalvelu.properties", "classpath:/test.properties" })
public class SpringTestAppConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource  =  new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    @Bean
    @Scope("singleton")
    public Audit audit() {
        return new Audit("osoitepalvelu", ApplicationType.VIRKAILIJA);
    }

}
