package com.tech.wixblog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("WixBlog API")
                              .version("1.0.0")
                              .description("WixBlog REST API Documentation")
                              .contact(new Contact()
                                               .name("WixBlog Team")
                                               .email("support@wixblog.com"))
                              .license(new License()
                                               .name("Apache 2.0")
                                               .url("http://springdoc.org")))
                .addServersItem(new Server().url("/api/v1"))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearer-jwt"))
                .components(new Components()
                                    .addSecuritySchemes("bearer-jwt",
                                                        new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .in(SecurityScheme.In.HEADER)
                                                                .name("Authorization")));
    }
}