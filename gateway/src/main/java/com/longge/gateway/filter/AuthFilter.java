package com.longge.gateway.filter;

import javax.annotation.Resource;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.longge.gateway.service.TestService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter {
	@Resource
	TestService testService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("auth filter is run");
		String key = exchange.getRequest().getQueryParams().getFirst("key");
		if(null == key) {
			log.info("no key, do chain");
		} else {
			String result = testService.call(exchange);
			log.info(result);
		}
		return chain.filter(exchange);
	}

}
