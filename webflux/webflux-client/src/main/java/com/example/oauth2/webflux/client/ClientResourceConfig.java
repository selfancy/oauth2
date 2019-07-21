package com.example.oauth2.webflux.client;

import org.springframework.context.annotation.Bean;
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
 * Created by mike on 2019-07-19
 */
@EnableWebFluxSecurity
public class ClientResourceConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/userinfo", "/jwt").hasAuthority("USER")
                .pathMatchers("/resource").hasAuthority("SCOPE_resource.read")
                .anyExchange().authenticated().and()
                .oauth2Client().and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        Map<String, Object> provider = new HashMap<>();
        provider.put("tokenUri", "http://www.server.com:9020/oauth/token");
        provider.put("authorizationUri", "http://www.server.com:9020/oauth/authorize");
        provider.put("userInfoUri", "http://www.server.com:9020/userinfo");
        provider.put("userNameAttribute", "name");

        ClientRegistration customClient = ClientRegistration.withRegistrationId("client")
                .clientId("client_id")
                .clientName("Custom Resource client")
                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
                .scope("openid", "resource.read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationUri("http://www.server.com:9020/oauth/authorize")
                .tokenUri("http://www.server.com:9020/oauth/token")
                .userInfoUri("http://www.server.com:9020/userinfo")
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
