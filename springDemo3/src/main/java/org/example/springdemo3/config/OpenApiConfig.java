package org.example.springdemo3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springDemoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("springDemo3 API 文档")
                        .description("springDemo3 项目的接口文档，基于 springdoc-openapi 自动生成。")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("springDemo3")
                                .url("https://example.org"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
