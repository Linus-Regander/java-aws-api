package com.imagerecognitioner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Image Recognition API",
                version = "0.8.0",
                description = "REST API for storing, moderating, and retrieving images."
        )
)
public class OpenApiConfig {
}