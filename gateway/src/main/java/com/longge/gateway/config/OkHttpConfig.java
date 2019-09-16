 package com.longge.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author roger yang
 * @date 9/16/2019
 */
@Component
@ConfigurationProperties(prefix = "okhttp")
@Getter
@Setter
public class OkHttpConfig {
    private Long connectTimeoutMs;
    private Long readTimeoutMs;
    private Long writeTimeoutMs;
    private Integer maxIdle;
    private Long keepAliveDurationSec;
}
