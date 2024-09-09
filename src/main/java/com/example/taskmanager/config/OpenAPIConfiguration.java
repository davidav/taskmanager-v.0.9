package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPIDescription(){

        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("Local env");

        Server productionServer = new Server();
        productionServer.setUrl("");
        productionServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setName("Andre Davidenko");
        contact.setEmail("davidenkav@gmail.com");

        License mitLicense = new License().name("GNU AGPLv3")
                .url("https://choosealicnese.com/licesnse/agpl-3.0/");

        Info info = new Info()
                .title("")
                .version("1.0")
                .contact(contact)
                .description("API for tasks management")
                .license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));

    }

}
