package com.example.oauth2.webflux.authorization.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by mike on 2019-07-19
 */
@EnableWebFluxSecurity
public class ReactiveServerSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user1 = User.builder().passwordEncoder(encoder::encode).username("mike").password("000")
                .roles("USER", "ADMIN", "CLIENT")
                .authorities("SCOPE_resource.read").build();

        UserDetails user2 = User.builder().passwordEncoder(encoder::encode).username("user").password("000")
                .roles("USER").build();
        return new MapReactiveUserDetailsService(user1, user2);
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyExchange().authenticated().and()
                .httpBasic().and()
                .formLogin().and()
                .oauth2Client().and()
                .build();
    }

}
