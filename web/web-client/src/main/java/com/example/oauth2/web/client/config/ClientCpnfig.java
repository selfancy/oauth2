//package com.example.oauth2.web.client.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
///**
// * Created by mike on 2019-07-23
// */
//@Configuration
//public class ClientCpnfig {
//
//        @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Configuration
//    @EnableAuthorizationServer //提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
//    static class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {
//
//            @Autowired
//            private PasswordEncoder encoder;
//
//
//        @Override
//        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//            oauthServer
//                    .tokenKeyAccess("permitAll()")
//                    .checkTokenAccess("isAuthenticated()") //allow check token
//                    .allowFormAuthenticationForClients();
//        }
//
//        @Override
//        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            clients.inMemory()
//                    .withClient("demoApp")
//                    .secret(encoder.encode("demoAppSecret"))
//                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
//                    .scopes("all")
//                    .resourceIds("oauth2-resource")
//                    .accessTokenValiditySeconds(1200)
//                    .refreshTokenValiditySeconds(50000)
//            .redirectUris("https://www.taobao.com");
//        }
//
//    }
//
//
//    @Configuration
//    @EnableResourceServer
//    static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    }
//}
