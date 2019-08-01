package com.example.oauth2.webflux.resource.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by mike on 2019-08-01
 */
@EnableWebFluxSecurity
public class WebfluxResourceServerConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .pathMatchers("/resource").hasAuthority("SCOPE_resource")
                .pathMatchers("/api").hasAuthority("SCOPE.api")
                .pathMatchers("/userinfo").hasAuthority("SCOPE_userinfo")
                .pathMatchers("/common").permitAll()
                .anyExchange().authenticated().and()
                .oauth2ResourceServer().and()
                .build();
    }
}
