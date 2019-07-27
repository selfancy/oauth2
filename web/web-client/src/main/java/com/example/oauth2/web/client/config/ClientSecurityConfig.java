package com.example.oauth2.web.client.config;

import com.example.oauth2.web.client.custom.CustomOAuth2ExceptionWebResponseExceptionTranslator;
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

/**
 * Created by mike on 2019/7/9
 */
@Configuration
@EnableResourceServer
public class ClientSecurityConfig {

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
                    .resourceIds("web-resource", "api-resource")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000)
                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
                    .redirectUris("http://www.server.com:8000/login/oauth2/code/client",
                            "http://www.server.com:8000/login/oauth2/code/github",
                            "http://www.server.com:9020/login/oauth2/code/client",
                            "http://www.server.com:9020/login/oauth2/code/github",
                            "https://www.taobao.com",
                            "https://www.jd.com");
        }
    }

    @Configuration
    static class CustomWebResourceServer extends ResourceServerConfigurerAdapter {

        private final AccessDeniedHandler accessDeniedHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        public CustomWebResourceServer(
                AccessDeniedHandler accessDeniedHandler,
                AuthenticationEntryPoint authenticationEntryPoint) {
            this.accessDeniedHandler = accessDeniedHandler;
            this.authenticationEntryPoint = authenticationEntryPoint;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("web-resource")
                    //有状态化,一次认证保存认证信息(适合web端)
                    //无状态化,每次请求需要认证(适合rest api)
                    .stateless(false)
                    //自定义Token异常信息,用于token校验失败返回信息
                    .authenticationEntryPoint(authenticationEntryPoint)
                    //授权异常处理
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                    // 跨域访问
                    .cors().and()
                    .anonymous().disable()
                    .authorizeRequests()
                    // web资源认证
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/resource").hasAuthority("resource")
                    .anyRequest().fullyAuthenticated().and()
                    .formLogin().and()
                    .httpBasic();
        }
    }

    @Configuration
    static class CustomApiResourceServer extends ResourceServerConfigurerAdapter {

        private final AccessDeniedHandler accessDeniedHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        public CustomApiResourceServer(
                AccessDeniedHandler accessDeniedHandler,
                AuthenticationEntryPoint authenticationEntryPoint) {
            this.accessDeniedHandler = accessDeniedHandler;
            this.authenticationEntryPoint = authenticationEntryPoint;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("api-resource")
                    //有状态化,一次认证保存认证信息(适合web端)
                    //无状态化,每次请求需要认证(适合rest api)
                    .stateless(true)
                    //自定义Token异常信息,用于token校验失败返回信息
                    .authenticationEntryPoint(authenticationEntryPoint)
                    //授权异常处理
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                    // 跨域访问
                    .cors().and()
                    .anonymous().disable()
                    .authorizeRequests()
                    // 客户端api授权资源认证
                    .antMatchers("/userinfo").access("#oauth2.hasScope('userinfo')")
                    .anyRequest().fullyAuthenticated().and()
                    .formLogin().and()
                    .httpBasic();
        }
    }

    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }
}
