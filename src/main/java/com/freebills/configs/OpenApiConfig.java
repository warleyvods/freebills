package com.freebills.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:9000");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info().title("Sicoob Secovicred RH-Web")
                        .description("Sicoob Secovicred - Employee Management System")
                        .version("v0.0.1")
                        .license(new License().name("All rights reserved").url("")))
                .externalDocs(new ExternalDocumentation());
    }

}
