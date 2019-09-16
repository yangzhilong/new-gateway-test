 package com.longge.gateway.configuration;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.longge.gateway.config.OkHttpConfig;
import com.longge.gateway.util.OkHttpUtils;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * @author roger yang
 * @date 9/16/2019
 */
@Configuration
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
}
