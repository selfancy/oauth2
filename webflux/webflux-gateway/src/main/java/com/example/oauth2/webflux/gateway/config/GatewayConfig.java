package com.example.oauth2.webflux.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Created by mike on 2019-08-02
 */
@Configuration
public class GatewayConfig {

    /**
     * 自定义全局拦截器
     * 拦截规则：在路由配置的filters之后
     */
    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            System.err.println(headers);
            return chain.filter(exchange);
        };
    }

}
