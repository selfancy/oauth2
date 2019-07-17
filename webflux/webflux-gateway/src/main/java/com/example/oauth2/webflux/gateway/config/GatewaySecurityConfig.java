package com.example.oauth2.webflux.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by mike on 2019/7/15
 */
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange().pathMatchers("/monitor/**").hasRole("ADMIN")
                .anyExchange().permitAll().and().cors().and()
                .httpBasic().and()
                .csrf().disable();
        return http.build();
    }
}
