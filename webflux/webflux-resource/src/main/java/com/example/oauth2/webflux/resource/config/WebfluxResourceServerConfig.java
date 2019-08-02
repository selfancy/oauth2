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
        //默认仅支持请求头：Authorization Bearer token 方式认证
        //设置支持uri参数access_token
        bearerTokenConverter.setAllowUriQueryParameter(true);
        http
                .authorizeExchange()
                // 允许actuator endpoints不进行认证
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // SCOPE_ 前缀对应认证服务器的客户端 scopes(...) 配置
                .pathMatchers("/resource").hasAuthority("SCOPE_resource")
                .pathMatchers("/api").hasAuthority("SCOPE_api")
                .pathMatchers("/userinfo").hasAuthority("SCOPE_userinfo")
                .pathMatchers("/baidu").hasAuthority("SCOPE_baidu")
                .pathMatchers("/common").permitAll()
                .anyExchange().authenticated().and()
                .oauth2ResourceServer()
                .bearerTokenConverter(bearerTokenConverter)
                .jwt();
        return http.build();
    }

}
