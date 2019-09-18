package com.longge.gateway.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.longge.gateway.fegin.TestFegin;
import com.longge.gateway.util.OkHttpUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {
	@Autowired
    private LoadBalancerClient loadBalancerClient;
	@Autowired
	private TestFegin testFegin;
	
	public String call(ServerWebExchange exchange) {
		ServiceInstance instance = loadBalancerClient.choose("server-a");
	    if(Objects.isNull(instance)) {
	        throw new NullPointerException("No instances are available");
	    }
	    
	    String result = testFegin.test();
	    log.info("server-a result is:{}", result);
	    
	    String uri = instance.getUri().toString();
	    uri = uri.concat("/test");
		return OkHttpUtils.get(uri, String.class);
	}
}
