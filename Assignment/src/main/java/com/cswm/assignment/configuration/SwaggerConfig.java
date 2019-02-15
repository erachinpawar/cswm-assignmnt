package com.cswm.assignment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cswm.assignment.ApplicationConstants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2) 
          .apiInfo(apiInfo())
          .select()   
          .apis(RequestHandlerSelectors.basePackage("com.cswm.assignment.controller"))              
          .paths(PathSelectors.any())                       
          .build();    
        
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        .title(ApplicationConstants.APPLICATION_NAME)
        .description(ApplicationConstants.APPLICATION_NAME_DESC)
        .version(ApplicationConstants.APPLICATION_NAME_VERSION)
        .build();
    }
}


