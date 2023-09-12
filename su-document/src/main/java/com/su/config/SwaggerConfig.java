package com.su.config;

import com.su.annotation.LoadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
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
@EnableSwagger2
@EnableOpenApi
@LoadFile(filePath = "classpath:/su-document.yml")
public class SwaggerConfig {

    @Value("${su.document.title}")
    private String title;

    @Value("${su.document.description}")
    private String description;

    @Value("${su.document.username}")
    private String username;

    @Value("${su.document.email}")
    private String email;

    @Value("${su.document.scaPacket}")
    private String scanPacket;

    @Value("${su.document.enable}")
    private Boolean enable;

    @Bean
    Docket docket() {
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
                .build();
    }

}
