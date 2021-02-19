package com.claudiodimauro.Scrape4Engineering.configurators;

import com.google.common.base.Predicates;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        //Return a prepared Docket instance
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.paths(PathSelectors.ant("/api/*/*"))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .apis(RequestHandlerSelectors.basePackage("com.claudiodimauro.Scrape4Engineering"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Scraping4Engineering",
                "Web Scraping Automation",
                "v. 0.3.1",
                "GNU Affero General Public License v3.0",
                new springfox.documentation.service.Contact("Claudio S. Di Mauro, Pierpaolo Venanzio", "https://web.unisa.it", "c.dimauro10@studenti.unisa.it - p.venanzio@studenti.unisa.it"),
                "GNU Affero General Public License v3.0",
                "https://github.com/csdm/Scraping4Engineering/blob/master/LICENSE",
                Collections.emptyList());
    }
}
