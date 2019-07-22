package com.example.oauth2.web.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Created by mike on 2019/7/9
 */
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("user")
                .password(passwordEncoder.encode("000"))
                .roles("USER", "ADMIN", "CLIENT")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Configuration
    @EnableAuthorizationServer
    static class CustomAuthorizationServer extends AuthorizationServerConfigurerAdapter {

        private final PasswordEncoder encoder;

        private final AuthenticationManager authenticationManager;

        private final UserDetailsService userDetailsService;

        public CustomAuthorizationServer(PasswordEncoder encoder,
                                         AuthenticationManager authenticationManager,
                                         UserDetailsService userDetailsService) {
            this.encoder = encoder;
            this.authenticationManager = authenticationManager;
            this.userDetailsService = userDetailsService;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            oauthServer.realm("oauth2-resource");
            oauthServer.tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client_1")
                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
                    .scopes("userinfo", "resource")
                    .resourceIds("oauth2-resource")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    .redirectUris("http://www.server.com:8000/login/oauth2/code/client",
                            "http://www.server.com:8000/login/oauth2/code/github",
                            "http://www.server.com:9020/login/oauth2/code/client",
                            "http://www.server.com:9020/login/oauth2/code/github",
                            "https://www.taobao.com");
        }
    }

    @Configuration
    @EnableResourceServer
    static class CustomResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().disable()
                    .authorizeRequests()
                    .antMatchers("/admin").hasAuthority("SCOPE_resource")
                    .antMatchers("/dba").hasRole("DBA")
                    .anyRequest().authenticated();
        }
    }

    @OAuth2ContextConfiguration
    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
//
//    /**
//     * 授权服务器令牌服务
//     */
//    @Bean
//    public AuthorizationServerTokenServices authorizationServerTokenServices(
//            TokenStore tokenStore, ClientDetailsService clientDetailsService,
//            ObjectProvider<TokenEnhancer> tokenEnhancer) {
//        DefaultTokenServices tokenService = new DefaultTokenServices();
//        tokenService.setTokenStore(tokenStore);
//        tokenService.setSupportRefreshToken(true);
//        tokenService.setClientDetailsService(clientDetailsService);
//        tokenService.setTokenEnhancer(tokenEnhancer.getIfAvailable());
//        tokenService.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30));
//        tokenService.setRefreshTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(60));
//        tokenService.setReuseRefreshToken(false);
//        return tokenService;
//    }
}
