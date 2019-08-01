//package com.example.oauth2.webflux.resource.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by mike on 2019-08-01
// */
//@Configuration
//public class ReactiveClientRegister {
//
//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        Map<String, Object> provider = new HashMap<>();
//        provider.put("tokenUri", "http://www.server.com:9020/oauth/token");
//        provider.put("authorizationUri", "http://www.server.com:9020/oauth2/authorization/reactive-client");
//        provider.put("userInfoUri", "http://www.server.com:9020/userinfo");
//        provider.put("userNameAttribute", "username");
//        provider.put("jwkSetUri", "http://www.server.com:9020/oauth/token_keys");
//
//        ClientRegistration customClient1 = ClientRegistration.withRegistrationId("reactive-client")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientId("reactive_client")
//                .clientName("My Reactive Client")
//                .clientSecret("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK")
//                .userNameAttributeName("name")
//                .scope("userinfo", "resource")
//                .redirectUriTemplate("http://www.server.com:9020/login/oauth2/code/reactive-client")
//                .authorizationUri("http://www.server.com:9020/oauth2/authorization/reactive-client")
//                .tokenUri("http://www.server.com:9020/oauth/token")
//                .userInfoUri("http://www.server.com:9020/userinfo")
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
