package com.beedigital.beestore.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @Test
    void customOpenAPI() {
        // GIVEN
        SwaggerConfig config = new SwaggerConfig();

        // WHEN
        OpenAPI openAPI = config.customOpenAPI();

        // THEN
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("BeeStore API");
        assertThat(openAPI.getInfo().getDescription())
                .isEqualTo("API REST de gestion des produits - Formation DevOps avec Spring Boot");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getInfo().getLicense()).isNotNull();
        assertThat(openAPI.getInfo().getLicense().getName()).isEqualTo("MIT");
    }
}