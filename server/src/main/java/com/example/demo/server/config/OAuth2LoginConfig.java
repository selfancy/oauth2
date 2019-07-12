//package com.example.demo.server.config;
//
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
//@Configuration
//public class OAuth2LoginConfig {
//
////    @Configuration
////    @EnableResourceServer
////    static class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {
////
////    }
//
//    @Order(SecurityProperties.BASIC_AUTH_ORDER)
//    @EnableWebSecurity
//    static class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
////            http.sessionManagement().disable()
////                    .authorizeRequests()
////                    .antMatchers("/login/**", "/custom").permitAll()
////                    .anyRequest().authenticated()
////                    .and()
//////                    .anonymous().disable()
////                    .oauth2Login().and().logout();
//            http.sessionManagement().disable()
////                    .anonymous().disable()
//                    .authorizeRequests()
////                    .antMatchers("/login", "/custom", "/").permitAll()
//                    .anyRequest().authenticated()
//                    .and()
//                    .oauth2Login();
//        }
//    }
//
////    @Bean
////    public ClientRegistrationRepository clientRegistrationRepository() {
////        return new InMemoryClientRegistrationRepository(this.googleClientRegistration(), this.customClientRegistration());
////    }
////
////    @Bean
////    public OAuth2AuthorizedClientService authorizedClientService(
////            ClientRegistrationRepository clientRegistrationRepository) {
////        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
////    }
////
////    @Bean
////    public OAuth2AuthorizedClientRepository authorizedClientRepository(
////            OAuth2AuthorizedClientService authorizedClientService) {
////        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
////    }
////
////    private ClientRegistration googleClientRegistration() {
////        return CommonOAuth2Provider.GITHUB.getBuilder("github")
////                .clientId("7f516f058038fbab149d")
////                .clientSecret("7bbe2d4fd83f6cb01b0691be067780513a1bc76b")
////                .scope("read:user,repo")
////                .build();
////    }
////
////    public static void main(String[] args) {
////        ClientRegistration registration = new OAuth2LoginConfig().customClientRegistration();
////        System.err.println(registration);
////    }
////
////    private ClientRegistration customClientRegistration() {
////        return ClientRegistration.withRegistrationId("custom")
////                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
////                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
////                .clientName("Custom")
////                .clientId("custom_client_id")
////                .clientSecret("666666")
////                .userNameAttributeName("id")
////                .scope("read:user,profile")
////                .redirectUriTemplate("http://localhost:80/login/oauth2/code")
////                .providerConfigurationMetadata(authProvider())
////                .build();
////    }
////
////    private Map<String, Object> authProvider() {
////        Map<String, Object> properties = new HashMap<>();
////        properties.put("tokenUrl", "http://localhost:8081/auth/oauth/token");
////        properties.put("authorizationUri", "http://localhost:8081/auth/oauth/authorize");
////        properties.put("userInfoUri", "http://localhost:8081/auth/user/me");
////        properties.put("userNameAttribute", "name");
////        return properties;
////    }
//}