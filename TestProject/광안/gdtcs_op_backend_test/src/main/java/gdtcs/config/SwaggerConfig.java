package gdtcs.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi jwtApi() {
        return GroupedOpenApi.builder()
                .group("운영단말시스템")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("광안대교 운영단말시스템 API 설명서")
                        .description("광안대교 운영단말시스템이 사용하는 API 목록과 설명이 있습니다.")
                        .version("v1.0.0"));
    }
}