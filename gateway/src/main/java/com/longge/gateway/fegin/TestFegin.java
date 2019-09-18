 package com.longge.gateway.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author roger yang
 * @date 9/18/2019
 */
@FeignClient(value = "server-a")
public interface TestFegin {
    
    @GetMapping("/test")
    String test();
}
