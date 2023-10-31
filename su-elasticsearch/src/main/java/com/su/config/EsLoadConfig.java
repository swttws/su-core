package com.su.config;

import com.su.annotation.LoadFile;
import org.springframework.context.annotation.Configuration;

/**
 * @author suweitao
 */
@Configuration
@LoadFile(filePath = "classpath:/su-es.yml")
public class EsLoadConfig {
}
