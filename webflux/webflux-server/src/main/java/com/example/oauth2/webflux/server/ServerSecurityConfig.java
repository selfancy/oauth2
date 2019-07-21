package com.example.oauth2.webflux.server;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2019-07-19
 */
@EnableWebFluxSecurity
public class ServerSecurityConfig {

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        UserDetails user1 = User.withDefaultPasswordEncoder().username("mike").password("000").roles("USER").authorities("openid").build();
        UserDetails user2 = User.withDefaultPasswordEncoder().username("user").password("000").authorities("openid", "SCOPE_resource.read").build();
        return new MapReactiveUserDetailsService(user1, user2);
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/userinfo").hasAuthority("USER")
                .pathMatchers("/resource").hasAuthority("SCOPE_resource.read")
                .anyExchange().authenticated().and()
                .httpBasic().and().formLogin();
        return http.build();
    }

//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        Map<String, Object> provider = new HashMap<>();
//        provider.put("tokenUri", "http://www.client.com:9010/oauth/token");
//        provider.put("authorizationUri", "http://www.client.com:9010/oauth/authorize");
//        provider.put("userInfoUri", "http://www.client.com:9010/userinfo");
//        provider.put("userNameAttribute", "name");
//
//        ClientRegistration customClient = ClientRegistration.withRegistrationId("client")
//                .clientId("client_id")
//                .clientName("Custom client")
//                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
//                .scope("openid", "resource.read")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationUri("http://www.client.com:9010/oauth/authorize")
//                .tokenUri("http://www.client.com:9010/oauth/token")
//                .userInfoUri("http://www.client.com:9010/userinfo")
//                .userNameAttributeName("name")
//                .redirectUriTemplate("{baseUrl}/{action}/oauth2/code/{registrationId}")
//                .providerConfigurationMetadata(provider)
//                .build();
//
//
//        ClientRegistration github = CommonOAuth2Provider.GITHUB.getBuilder("github")
//                .clientId("7f516f058038fbab149d")
//                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
//                .scope("read:user,repo")
//                .build();
//        return new InMemoryReactiveClientRegistrationRepository(customClient, github);
//    }
//
//    @Bean
//    public ReactiveOAuth2AuthorizedClientService authorizedClientService(
//            ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
//    }
//
//    @Bean
//    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository(
//            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
//        return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
//    }
}
