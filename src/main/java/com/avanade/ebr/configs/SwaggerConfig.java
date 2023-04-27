package com.avanade.ebr.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket productApi() {

        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage(
                        "com.avanade.ebr"))
                .paths(regex("/ebr.*"))
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfo("API Document",
                "An Advanced Dungeons &amp; Dragons (AD&amp;D) style RPG API.",
                "0.0.1",
                "Terms of Service",
                new Contact("Jean Michel",
                        "https://github.com/jmoliver",
                        "jeanmicheloliver@hotmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html",
                new ArrayList<>());
    }
}
