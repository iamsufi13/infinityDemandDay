//package com.contenttree.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.service.ParameterType;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.schema.ModelRef;
//
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.contenttree")) // Update with your package
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(
//                        List.of(new Parameter()
//                                .name("Authorization")
//                                .description("Bearer JWT token")
//                                .required(false)
//                                .in(ParameterType.HEADER)
//                                .modelRef(new ModelRef("string")) // Ensure correct import
//                        )
//                )
//                .apiInfo(apiInfo()); // Add API info
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "My API Title",
//                "API Description",
//                "API TOS",
//                "Terms of service",
//                "contact@myapi.com",
//                "License of API",
//                "API license URL");
//    }
//}
