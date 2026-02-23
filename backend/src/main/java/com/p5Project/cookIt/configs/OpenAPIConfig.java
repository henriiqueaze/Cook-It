package com.p5Project.cookIt.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Cook-It API")
                .version("V1")
                .description("O Cook It é um aplicativo/web app que ajuda você a encontrar receitas com os ingredientes que já tem em casa. Você informa o que tem disponível e o sistema mostra receitas cadastradas por outros usuários que usam esses ingredientes.")
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/license/mit")));
    }
}
