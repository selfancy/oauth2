package com.example.oauth2.web.authorization.config;

import com.example.oauth2.web.authorization.custom.CustomOAuth2ExceptionWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class WebAuthorizationServerSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("user")
                .password(passwordEncoder.encode("000"))
                .roles("USER", "ADMIN", "CLIENT")
                .authorities("resource")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Configuration
    @EnableAuthorizationServer
    static class CustomAuthorizationServer extends AuthorizationServerConfigurerAdapter {

        private final PasswordEncoder encoder;

        private final AuthenticationManager authenticationManager;

        private final UserDetailsService userDetailsService;

        private final TokenStore tokenStore;

        private final AccessTokenConverter accessTokenConverter;

        public CustomAuthorizationServer(PasswordEncoder encoder,
                                         AuthenticationManager authenticationManager,
                                         UserDetailsService userDetailsService,
                                         TokenStore tokenStore,
                                         AccessTokenConverter accessTokenConverter) {
            this.encoder = encoder;
            this.authenticationManager = authenticationManager;
            this.userDetailsService = userDetailsService;
            this.tokenStore = tokenStore;
            this.accessTokenConverter = accessTokenConverter;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            oauthServer.tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints
                    .tokenStore(tokenStore)
                    .accessTokenConverter(accessTokenConverter)
                    .authenticationManager(authenticationManager)
                    // password模式支持
                    .userDetailsService(userDetailsService)
                    .exceptionTranslator(new CustomOAuth2ExceptionWebResponseExceptionTranslator());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("web-client")
                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
                    .scopes("userinfo", "resource", "api")
                    .resourceIds("web-resource")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    .redirectUris("http://www.client.com:8010/login/oauth2/code/client",
                            "http://www.resource.com:8020/common",
                            "https://www.taobao.com",
                            "https://www.jd.com")
                    .and()
                    .withClient("reactive-client")
                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
                    .scopes("userinfo", "resource")
                    .resourceIds("webflux-resource")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    .redirectUris("http://www.server.com:9020/login/oauth2/code/reactive-client",
                            "http://www.server.com:8000/userinfo",
                            "https://www.taobao.com",
                            "https://www.jd.com");
        }

    }

    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final AccessDeniedHandler accessDeniedHandler;

        public WebSecurityConfig(AccessDeniedHandler accessDeniedHandler) {
            this.accessDeniedHandler = accessDeniedHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .requestMatchers()
                    .mvcMatchers("/.well-known/jwks.json")
                    .antMatchers("/login", "/oauth/authorize", "/oauth/token")
                    .antMatchers("/**")
                    .and()
                    .authorizeRequests()
                    .mvcMatchers("/.well-known/jwks.json").permitAll()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .formLogin()
                    .and()
                    .httpBasic()
                    .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

}