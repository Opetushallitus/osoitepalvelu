package fi.vm.sade.osoitepalvelu.kooste.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spi.DocumentationType;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerTwoConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("fi.vm.sade.osoitepalvelu.kooste.mvc"))
                .paths(regex("/email.*"))
                .build()
                .apiInfo(metaData());

    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    private ApiInfo metaData() {
        return new ApiInfo(
                "Osoitepalvelu", /* title */
                "Osoitepalvelu on Opetushallituksen työntekijöille tarkoitettu osoitetietojen hakemista varten." +
                        "Palvelussa on käyttöliittymä, jonka avulla työntekijä voi hakea osoitetietoja eri" +
                        " hakukriteereillä. Hakukriteerit voi myös tallentaa haluamalleen nimelle myöhempää " +
                        "tarvetta varten. Tiedot koostetaan muista palveluista.",
                "1.0",
                null, /* TOS URL */
                null, /* Contact */
                "EUPL", /* license */
                "http://www.osor.eu/eupl/", /* license URL */
                new ArrayList<>()
        );
    }
}