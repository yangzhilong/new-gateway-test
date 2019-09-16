 package com.longge.gateway.configuration;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.longge.gateway.util.OkHttpUtils;

import lombok.Getter;
import lombok.Setter;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * @author roger yang
 * @date 9/16/2019
 */
public class OkHttpConfiguration {
    @Resource
    private OkHttpConfig okHttpConfig;
    
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .connectTimeout(okHttpConfig.getConnectTimeoutMs(), TimeUnit.MILLISECONDS)
                .readTimeout(okHttpConfig.getReadTimeoutMs(), TimeUnit.MILLISECONDS)
                .writeTimeout(okHttpConfig.getWriteTimeoutMs(),TimeUnit.MILLISECONDS)
                .build();
        
        OkHttpUtils.setOkHttpClient(client);
        return client;
    }
    
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(okHttpConfig.getMaxIdle(), okHttpConfig.getKeepAliveDurationSec(), TimeUnit.SECONDS);
    }
    
    @Component
    @ConfigurationProperties(prefix = "okhttp")
    @Getter
    @Setter
    static class OkHttpConfig {
        private Long connectTimeoutMs;
        private Long readTimeoutMs;
        private Long writeTimeoutMs;
        private Integer maxIdle;
        private Long keepAliveDurationSec;
    }
}
