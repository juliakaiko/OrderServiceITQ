package com.itq.myproject.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api (){
        return new OpenAPI().servers(
                List.of(new Server().url("http://localhost:8080"))
        ).info(
                new Info().title("Order Servise API")
        );
    }
    //http://localhost:8080/swagger-ui/index.html
}
