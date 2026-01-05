package com.example.multi_tanent.warehouse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI warehouseOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8585");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Warehouse Team");
        contact.setEmail("warehouse@company.com");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Warehouse Management System API")
                .version("1.0.0")
                .description(
                        "Complete WMS API including Inbound, Outbound, Inventory, RFID, and Requisition Management")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
