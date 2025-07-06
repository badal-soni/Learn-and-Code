package com.intimetec.newsaggregation.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApi() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:2020");
        localServer.setDescription("Server URL in Local environment");

        Info info = new Info()
                .title("NEWS AGGREGATION API")
                .version("1.0")
                .description("NEWS API");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }

}
