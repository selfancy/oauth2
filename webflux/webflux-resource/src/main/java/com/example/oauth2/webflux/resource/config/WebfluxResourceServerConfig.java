package com.example.oauth2.webflux.resource.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by mike on 2019-08-01
 */
@EnableWebFluxSecurity
public class WebfluxResourceServerConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        ServerBearerTokenAuthenticationConverter bearerTokenConverter = new ServerBearerTokenAuthenticationConverter();
        bearerTokenConverter.setAllowUriQueryParameter(true);   //支持uri参数access_token
        http
                .authorizeExchange()
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .pathMatchers("/resource").hasAuthority("SCOPE_resource")
                .pathMatchers("/api").hasAuthority("SCOPE_api")
                .pathMatchers("/userinfo").hasAuthority("SCOPE_userinfo")
                .pathMatchers("/common").permitAll()
                .anyExchange().authenticated().and()
                .oauth2ResourceServer()
                .bearerTokenConverter(bearerTokenConverter)
                .jwt();
        return http.build();
    }

}
