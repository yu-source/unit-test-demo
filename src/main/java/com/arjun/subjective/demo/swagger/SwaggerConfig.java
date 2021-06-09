package com.arjun.subjective.demo.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.arjun.subjective.demo"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("SpringBoot利用Swagger构建API文档")
                // 描述
                .description("使用RestFul风格, 创建人：arjun")
                // 许可证
                .license("MIT")
                // 许可证地址
                .licenseUrl("www.xx.com")
                // 服务端地址
                .termsOfServiceUrl("https://github.com/yu-source")
                // 版本
                .version("version 1.0")
                // 联系信息
                .contact(new Contact("arjun","https://github.com/yu-source","xxxxx@163.com"))
                .build();
    }
}