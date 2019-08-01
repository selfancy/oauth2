//package com.example.oauth2.web.client.sso;
//
//import com.example.oauth2.web.resource.config.custom.CustomWebResponseExceptionTranslator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
//import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
//import org.springframework.security.web.context.SecurityContextPersistenceFilter;
//
///**
// * Created by mike on 2019-07-29
// */
//@Configuration
//public class SsoClientSecurityConfig {
//
//    @Bean
//    OAuth2RestTemplate oauth2RestTemplate(
//            @Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext,
//            OAuth2ProtectedResourceDetails details) {
//        return new OAuth2RestTemplate(details, oauth2ClientContext);
//    }
//
//
//    @EnableWebSecurity
//    @EnableOAuth2Sso  //@EnableOAuth2Sso注解来开启SSO
//    static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        private SimpleCORSFilter simpleCORSFilter;
//
//        @Value("${oauth2-authorization}")
//        private String oauthServerUrl;
//
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            //解决静态资源被拦截的问题
//            web.ignoring().antMatchers("/assets/**");
//            web.ignoring().antMatchers("/favicon.ico");
//
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .requestMatchers()
//                    .antMatchers("/oauth/**", "/login", "/index")
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/oauth/**").authenticated()
//                    .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
//                    .and()
//                    .formLogin()
//                    .permitAll()
//                    .loginProcessingUrl("/index");
//            http.addFilterBefore(simpleCORSFilter, SecurityContextPersistenceFilter.class);
//        }
//    }
//
//    @Configuration
//    @EnableResourceServer   //注解来开启资源服务器
//    static class ResourceConfiguration extends ResourceServerConfigurerAdapter {
//
//        private static final String RESOURCE_ID = "resource_id";
//        @Autowired
//        @Qualifier("jwtTokenServices")
//        private DefaultTokenServices tokenServices;
//        @Autowired
//        @Qualifier("jwtTokenStore")
//        private TokenStore tokenStore;
//
//
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources) {
//            resources.resourceId(RESOURCE_ID).stateless(true).tokenServices(tokenServices);
//        }
//
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//
//            //如果 启用 http.addFilterBefore(oAuth2AuthenticationFilter,AbstractPreAuthenticatedProcessingFilter.class) 代码 则需要启用下面被注释的代码
//            OAuth2AuthenticationProcessingFilter oAuth2AuthenticationFilter = new OAuth2AuthenticationProcessingFilter();
//            OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
//            oAuth2AuthenticationEntryPoint.setExceptionTranslator(new CustomWebResponseExceptionTranslator());
//            oAuth2AuthenticationFilter.setAuthenticationEntryPoint(oAuth2AuthenticationEntryPoint);
//            OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
//            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//            defaultTokenServices.setTokenStore(tokenStore);
//            oAuth2AuthenticationManager.setTokenServices(defaultTokenServices);
//            oAuth2AuthenticationFilter.setAuthenticationManager(oAuth2AuthenticationManager);
//
//            // 配置那些资源需要保护的
//            http.csrf().disable()
//                    .requestMatchers().antMatchers("/api/**")
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/api/**").authenticated()
//                    .and()
//                    .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
//            http.addFilterBefore(oAuth2AuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class); // 这种方式也可以达到token校验失败后自定义返回数据格式  使用此方式需要将上面的代码启用
//        }
//    }
//}
