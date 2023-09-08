package com.su.config;

import com.su.processor.LoadFileProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author suweitao
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoadFileConfig {

    @Bean("loadFileProcessor")
    public LoadFileProcessor loadFileProcessor(){
        return new LoadFileProcessor();
    }

}
