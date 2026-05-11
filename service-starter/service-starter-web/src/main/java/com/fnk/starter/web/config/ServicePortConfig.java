package com.fnk.starter.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Enigma
 */

@Configuration
@ConfigurationProperties(prefix = "service.port")
@Data
public class ServicePortConfig {

    private Integer admin;
    // 其他端
}
