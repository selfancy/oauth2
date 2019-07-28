package com.example.oauth2.web.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2019-07-29
 */
@Configuration
//@EnableResourceServer
public class ClientConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(PasswordEncoder encoder) {
        return new InMemoryClientRegistrationRepository(
                this.githubClientRegistration(), this.customClientRegistration(encoder));
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("7f516f058038fbab149d")
                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
                .scope("read:user,repo")
                .build();
    }

    private ClientRegistration customClientRegistration(PasswordEncoder encoder) {
        return ClientRegistration.withRegistrationId("client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientName("My Client")
                .clientId("client_1")
                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
                .userNameAttributeName("name")
                .scope("userinfo", "resource")
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("http://www.server.com:8000/oauth/authorize")
                .tokenUri("http://www.server.com:8000/oauth/token")
                .userInfoUri("http://www.server.com:8000/userinfo")
                .providerConfigurationMetadata(authProvider())
                .build();
    }

    /**
     * 认证服务提供者
     */
    private Map<String, Object> authProvider() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("tokenUrl", "http://www.server.com:8000/oauth/token");
        properties.put("authorizationUri", "http://www.server.com:8000/oauth/authorize");
        properties.put("userInfoUri", "http://www.server.com:8000/userinfo");
        properties.put("userNameAttribute", "username");
        properties.put("jwkSetUri", "http://www.server.com:8000/oauth/token_keys");
        return properties;
    }
}
