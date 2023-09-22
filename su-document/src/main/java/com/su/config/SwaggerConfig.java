package com.su.config;

import com.su.annotation.LoadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author suweitao
 * swagger配置类
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${document.title}")
    private String title;

    @Value("${document.description}")
    private String description;

    @Value("${document.username}")
    private String username;

    @Value("${document.email}")
    private String email;

    @Value("${document.scanPacket}")
    private String scanPacket;

    @Value("${document.enable}")
    private Boolean enable;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .enable(enable)
                //配置网站的基本信息
                .apiInfo(new ApiInfoBuilder()
                        //网站标题
                        .title(title)
                        //标题后面的版本号
                        .version("v1.0")
                        .description(description)
                        //联系人信息
                        .contact(new Contact(username, "none", email))
                        .build())
                .select()
                //指定接口的位置
                .apis(RequestHandlerSelectors.basePackage(scanPacket))
                .paths(PathSelectors.any())
                .build();
    }

}
