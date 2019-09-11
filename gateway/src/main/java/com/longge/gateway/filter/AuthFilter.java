package com.longge.gateway.filter;

import javax.annotation.Resource;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.longge.gateway.service.TestService;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {
	@Resource
	TestService testService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("222222222222222");
		String result = testService.call(exchange);
		System.out.println(result);
		return chain.filter(exchange);
	}

}
