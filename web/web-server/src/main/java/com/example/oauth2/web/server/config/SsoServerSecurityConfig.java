package com.example.oauth2.web.server.config;

import com.example.oauth2.web.server.config.custom.CustomOAuth2ExceptionWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SsoServerSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
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

        public CustomAuthorizationServer(PasswordEncoder encoder,
                                         AuthenticationManager authenticationManager,
                                         UserDetailsService userDetailsService) {
            this.encoder = encoder;
            this.authenticationManager = authenticationManager;
            this.userDetailsService = userDetailsService;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            oauthServer.tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.authenticationManager(authenticationManager)
                    // password模式支持
                    .userDetailsService(userDetailsService)
                    .exceptionTranslator(new CustomOAuth2ExceptionWebResponseExceptionTranslator());
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
                    .redirectUris("http://www.client.com:8010/login/oauth2/code/client",
                            "http://www.server.com:8000/userinfo",
                            "http://www.server.com:8000/login/oauth2/code/github",
                            "http://www.server.com:9020/login/oauth2/code/client",
                            "http://www.server.com:9020/login/oauth2/code/github",
                            "https://www.taobao.com",
                            "https://www.jd.com");
        }
    }

    @Configuration
    @EnableResourceServer
    static class ServerResourceServer extends ResourceServerConfigurerAdapter {

        private final AccessDeniedHandler accessDeniedHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        public ServerResourceServer(AccessDeniedHandler accessDeniedHandler,
                                    AuthenticationEntryPoint authenticationEntryPoint) {
            this.accessDeniedHandler = accessDeniedHandler;
            this.authenticationEntryPoint = authenticationEntryPoint;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("oauth2-resource")
                    //此处是关键，默认stateless=true，只支持access_token形式，
                    // OAuth2客户端连接需要使用session，所以需要设置成false以支持session授权
                    .stateless(false)
                    //自定义Token异常信息,用于token校验失败返回信息
                    .authenticationEntryPoint(authenticationEntryPoint)
                    //授权异常处理
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .httpBasic();
            //需要的时候创建session，支持从session中获取认证信息，ResourceServerConfiguration中
            //session创建策略是stateless不使用，这里其覆盖配置可创建session
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }
    }

    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AccessDeniedHandler accessDeniedHandler;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
        }
    }

}