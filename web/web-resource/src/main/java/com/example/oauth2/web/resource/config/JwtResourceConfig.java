//package com.example.oauth2.web.resource.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
//
///**
// * Created by mike on 2019-07-31
// */
////@Configuration
//public class JwtResourceConfig {
//
//    @Bean
//    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
//                                         String jwkSetUri) {
//        return new NimbusJwtDecoderJwkSupport(jwkSetUri);
//    }
//
////    @EnableWebSecurity
//    static class ResourceSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        private final JwtDecoder jwtDecoder;
//
//        public ResourceSecurityConfig(JwtDecoder jwtDecoder) {
//            this.jwtDecoder = jwtDecoder;
//        }
//
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .mvcMatchers("/resource").hasAuthority("SCOPE_resource")
//                    .mvcMatchers("/api").hasAuthority("SCOPE_api")
//                    .anyRequest().authenticated()
//                    .and()
//                    .oauth2ResourceServer()
//                    .jwt()
//                    .decoder(jwtDecoder);
//        }
//    }
//}
