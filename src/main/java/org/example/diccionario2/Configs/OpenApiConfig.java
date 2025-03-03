package org.example.diccionario2.Configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Diccionario2",
                version = "1.0.0",
                description = "CRUD de un diccionario"
        )
)
public class OpenApiConfig {

}
