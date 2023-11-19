package com.cbfacademy.apiassessment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    @Value("${lmsalaudeen.openapi.local-url}")
    private String localhost;

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(localhost);
        server.setDescription("Local ENV");

        Contact contact = new Contact();
        contact.name("Latifah Mojisola Salaudeen");
        contact.setUrl("https://github.com/lmsalaudeen");

        Info info = new Info()
            .title("Blog Management API: REST API with SpringBoot (CBF Assessment)")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints to manage blogs.");

        return new OpenAPI().info(info);
  }
}
