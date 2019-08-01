package com.example.oauth2.web.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Created by mike on 2019-07-30
 */
@Configuration
public class WebResourceServerConfig {

    @Configuration
    @EnableResourceServer
    static class ResourceConfig extends ResourceServerConfigurerAdapter {

        private final ResourceServerTokenServices remoteTokenServices;

        private final AccessDeniedHandler accessDeniedHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        public ResourceConfig(ResourceServerTokenServices remoteTokenServices,
                              AccessDeniedHandler accessDeniedHandler,
                              AuthenticationEntryPoint authenticationEntryPoint) {
            this.remoteTokenServices = remoteTokenServices;
            this.accessDeniedHandler = accessDeniedHandler;
            this.authenticationEntryPoint = authenticationEntryPoint;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("web-resource")
                    //无状态化,每次访问都需认证
                    .stateless(true)
                    //远程认证服务器认证token
                    .tokenServices(remoteTokenServices)
                    //自定义Token异常信息,用于token校验失败返回信息
                    .authenticationEntryPoint(authenticationEntryPoint)
                    //授权异常处理
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //不创建session
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/resource").access("#oauth2.hasScope('resource')")
                    .antMatchers("/api").access("#oauth2.hasScope('api')")
                    .antMatchers("/userinfo").access("#oauth2.hasScope('userinfo') || #oauth2.clientHasRole('USER')")
                    .antMatchers("/common").permitAll()
                    .anyRequest().authenticated();
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
            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/favicon.ico");
        }
    }
}
