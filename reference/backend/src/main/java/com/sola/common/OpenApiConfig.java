package com.sola.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI solaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sola API (Reference)")
                        .version("v1")
                        .description("Bộ API đối soát Chặng 0–1: các endpoint công khai cho Phòng nghe & Khám phá."));
    }
}
