package com.example.oauth2.webflux.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2019/7/15
 */
@EnableWebFluxSecurity
public class WebfluxSecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/client/**").hasRole("GITHUB")
                .pathMatchers("/github/**").hasRole("GITHUB")
                .pathMatchers("/userinfo/**").hasRole("USER")
                .anyExchange().authenticated().and()
                .oauth2ResourceServer().and()
                .oauth2Login().and()
                .cors().and()
                .build();
    }

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        Map<String, Object> provider = new HashMap<>();
        provider.put("tokenUri", "http://www.client.com:8010/oauth/token");
        provider.put("authorizationUri", "http://www.client.com:8010/oauth/authorize");
        provider.put("userInfoUri", "http://www.client.com:8010/userinfo");
        provider.put("userNameAttribute", "name");

        ClientRegistration customClient = ClientRegistration.withRegistrationId("client")
                .clientId("client_id")
                .clientName("Custom client")
                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
                .scope("userinfo")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationUri("http://www.client.com:8010/oauth/authorize")
                .tokenUri("http://www.client.com:8010/oauth/token")
                .userInfoUri("http://www.client.com:8010/userinfo")
                .userNameAttributeName("name")
                .redirectUriTemplate("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .providerConfigurationMetadata(provider)
                .build();


        ClientRegistration github = CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("7f516f058038fbab149d")
                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
                .scope("read:user,repo")
                .build();
        return new InMemoryReactiveClientRegistrationRepository(customClient, github);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService authorizedClientService(
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository(
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
    }

}
