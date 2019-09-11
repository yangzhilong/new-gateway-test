package com.longge.gateway.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.netflix.loadbalancer.Server;

@Service
public class TestService {
	@Autowired
    private LoadBalancerClient loadBalancerClient;
	
	@LoadBalanced
	public String call(ServerWebExchange exchange) {
		ServiceInstance instance = loadBalancerClient.choose("server-a");
		System.out.println(instance.getUri().toString());
		System.out.println(instance.getHost());
		
		SpringClientFactory factory = exchange.getApplicationContext().getBean(SpringClientFactory.class);
		List<Server> servers = factory.getLoadBalancer("server-a").getReachableServers();
		System.out.println(servers.get(0).getHost() + ":" + servers.get(0).getHostPort());
		try {
			URL url = new URL("http://www.baidu.com");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}

			br.close();
			
			Thread.sleep(5000L);
			return sb.toString();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NULL";
	}
}
