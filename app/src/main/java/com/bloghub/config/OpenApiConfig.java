package com.bloghub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BlogHub API")
                        .version("1.0")
                        .description("API documentation for BlogHub application")
                        .contact(new Contact()
                                .name("BlogHub Support")
                                .email("rajputsanju2622@gmail.com")
                                .url("https://sangram-rajpoot.github.io/Portfolio/")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://sangram-rajpoot.github.io/Portfolio/"))
                );
    }
}
