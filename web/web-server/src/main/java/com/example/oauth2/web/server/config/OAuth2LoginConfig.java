package com.example.demo.server.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OAuth2LoginConfig {

//    @Order(SecurityProperties.BASIC_AUTH_ORDER)
//    @EnableWebSecurity
    @Configuration
    @EnableAuthorizationServer
    static class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login();
        }
    }

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.googleClientRegistration(), this.customClientRegistration());
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientService authorizedClientService(
//            ClientRegistrationRepository clientRegistrationRepository) {
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientRepository authorizedClientRepository(
//            OAuth2AuthorizedClientService authorizedClientService) {
//        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
//    }
//
//    private ClientRegistration googleClientRegistration() {
//        return CommonOAuth2Provider.GITHUB.getBuilder("github")
//                .clientId("7f516f058038fbab149d")
//                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
//                .scope("read:user,repo")
//                .build();
//    }
//
//    public static void main(String[] args) {
//        ClientRegistration registration = new OAuth2LoginConfig().customClientRegistration();
//        System.err.println(registration);
//    }
//
//    private ClientRegistration customClientRegistration() {
//        return ClientRegistration.withRegistrationId("custom")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientName("Custom")
//                .clientId("custom_client_id")
//                .clientSecret("666666")
//                .userNameAttributeName("id")
//                .scope("read:user,profile")
//                .redirectUriTemplate("http://localhost:80/login/oauth2/code")
//                .providerConfigurationMetadata(authProvider())
//                .build();
//    }
//
//    private Map<String, Object> authProvider() {
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("tokenUrl", "http://localhost:8081/auth/oauth/token");
//        properties.put("authorizationUri", "http://localhost:8081/auth/oauth/authorize");
//        properties.put("userInfoUri", "http://localhost:8081/auth/user/me");
//        properties.put("userNameAttribute", "name");
//        return properties;
//    }
}