package com.fnk.starter.doc.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerBasicConfig {

    @Value("${sa-token.token-name}")
    private String tokenName;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${service.version}")
    private String version;


    private Components components() {
        return new Components().addSecuritySchemes(tokenName, new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name(tokenName));
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(components())
                .addSecurityItem(new SecurityRequirement().addList(tokenName))
                .info(new Info()
                        .title(applicationName + " API")
                        .contact(new Contact().name("Enigma"))
                        .version(version)
                        .description("API文档")
                );

    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new HeaderParameter().name(tokenName));
            return operation;
        };
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * API分组
     * 可进行拆分
     *
     * @return
     */
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("system")
                .pathsToMatch("/system/**")
                .build();
    }
}
