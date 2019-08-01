//package com.example.oauth2.webflux.resource.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.reactive.config.WebFluxConfigurer;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by mike on 2019-07-19
// */
//@Configuration
//public class ReactiveClientConfig {
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public ReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
////        UserDetails user1 = User.builder().passwordEncoder(encoder::encode).username("mike").password("000")
////                .roles("USER", "ADMIN", "CLIENT")
////                .authorities("SCOPE_resource.read").build();
////
////        UserDetails user2 = User.builder().passwordEncoder(encoder::encode).username("user").password("000")
////                .roles("USER").build();
////        return new MapReactiveUserDetailsService(user1, user2);
////    }
////
////    @Bean
////    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
////        http
////                .authorizeExchange()
////                .anyExchange().authenticated().and()
////                .oauth2Login();
////        return http.build();
////    }
//
//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        Map<String, Object> provider = new HashMap<>();
//        provider.put("tokenUri", "http://www.server.com:8000/oauth/token");
//        provider.put("authorizationUri", "http://www.server.com:8000/oauth/authorize");
//        provider.put("userInfoUri", "http://www.server.com:8000/userinfo");
//        provider.put("userNameAttribute", "name");
//
//        ClientRegistration customClient1 = ClientRegistration.withRegistrationId("web-client")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientId("web-client")
//                .clientName("My Reactive Client")
//                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
//                .userNameAttributeName("name")
//                .scope("userinfo", "resource")
//                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
//                .authorizationUri("http://www.resource.com:9020/oauth2/authorization/web-client")
////                .authorizationUri("/oauth2/authorization/{registrationId}")
////                .authorizationUri("http://www.server.com:8000/oauth2/authorize")
//                .tokenUri("http://www.server.com:8000/oauth/token")
//                .userInfoUri("http://www.server.8000:9020/userinfo")
//                .providerConfigurationMetadata(provider)
//                .build();
//
//        ClientRegistration github = CommonOAuth2Provider.GITHUB.getBuilder("github")
//                .clientId("7f516f058038fbab149d")
//                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
//                .scope("read:user,repo")
//                .build();
//        return new InMemoryReactiveClientRegistrationRepository(customClient1, github);
//    }
//
//    @Bean
//    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
//                        ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
//        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
//                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);
//        oauth.setDefaultOAuth2AuthorizedClient(true);
//        return WebClient.builder()
//                .filter(oauth)
//                .build();
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
//}
