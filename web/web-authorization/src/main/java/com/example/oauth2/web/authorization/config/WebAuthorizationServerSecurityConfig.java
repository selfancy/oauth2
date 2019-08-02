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

/**
 * Web认证服务器安全配置
 */
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

    /**
     * 认证配置
     */
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
                    // 允许客户端表单认证
                    .allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints
                    .tokenStore(tokenStore)
                    // jwt支持
                    .accessTokenConverter(accessTokenConverter)
                    .authenticationManager(authenticationManager)
                    // password模式支持
                    .userDetailsService(userDetailsService)
                    // 自定义OAuth2异常转换
                    .exceptionTranslator(new CustomOAuth2ExceptionWebResponseExceptionTranslator());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("web-client")
                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
                    .scopes("userinfo", "resource")
                    .resourceIds("web-resource")
                    // 允许自动授权，否则将跳转授权页面手动授权
                    .autoApprove(true)
                    // token失效时间
                    .accessTokenValiditySeconds(1200)
                    // 刷新token失效时间
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    // 重定向路径，此处很重要：客户端请求认证服务器，需要配置重定向路径
                    .redirectUris(
                            "http://www.client.com:8010/login/oauth2/code/client",
                            "http://www.resource.com:8020/common",
                            "https://www.taobao.com",
                            "https://www.jd.com")
                    .and()
                    .withClient("webflux-client")
                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
                    .scopes("baidu", "userinfo", "resource", "api")
                    .resourceIds("webflux-resource")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    .redirectUris(
                            "http://www.server.com:9020/login/oauth2/code/webflux-client",
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
                    // 匹配oauth自带认证路径
                    .antMatchers("/login", "/oauth/**")
                    // 匹配其他所有路径
                    .antMatchers("/**")
                    .and()
                    // 配置需要认证的路径
                    .authorizeRequests()
                    // 所有请求需要认证才能被访问  fullyAuthenticated：禁用anonymous和rememberMe
                    .anyRequest().fullyAuthenticated()
                    .and()
                    // 允许表单登录
                    .formLogin()
                    .and()
                    // 允许http basic认证：Authorization basic Base64.encode('username:password')
                    .httpBasic()
                    .and()
                    // 认证异常处理
                    .exceptionHandling()
                    // 认证权限不足处理
                    .accessDeniedHandler(accessDeniedHandler);
        }

        /**
         * 配置默认AuthenticationManager
         */
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

}