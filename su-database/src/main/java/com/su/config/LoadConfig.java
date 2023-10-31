package com.su.config;

import com.su.annotation.LoadFile;
import org.springframework.context.annotation.Configuration;

/**
 * @author suweitao
 */
@Configuration
@LoadFile(filePath = "classpath:/su-db.yml")
public class LoadConfig {
}
