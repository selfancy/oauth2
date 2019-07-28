//package com.example.oauth2.web.client.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.token.TokenService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by mike on 2019-07-28
// */
//@Configuration
//@EnableResourceServer
//public class ClentTestConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
////        UserDetails userDetails = User.withUsername("user")
////                .password(passwordEncoder.encode("000"))
////                .roles("USER", "ADMIN", "CLIENT")
////                .authorities("resource")
////                .build();
////        return new InMemoryUserDetailsManager(userDetails);
////    }
//
////    @Configuration
////    @EnableAuthorizationServer
////    static class CustomAuthorizationServer extends AuthorizationServerConfigurerAdapter {
////
////        private final PasswordEncoder encoder;
////
////        private final AuthenticationManager authenticationManager;
////
////        private final UserDetailsService userDetailsService;
////
////        public CustomAuthorizationServer(PasswordEncoder encoder,
////                                         AuthenticationManager authenticationManager,
////                                         UserDetailsService userDetailsService) {
////            this.encoder = encoder;
////            this.authenticationManager = authenticationManager;
////            this.userDetailsService = userDetailsService;
////        }
////
////        @Override
////        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
////            oauthServer.tokenKeyAccess("permitAll()")
////                    .checkTokenAccess("isAuthenticated()")
////                    .allowFormAuthenticationForClients();
////        }
////
////        @Override
////        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
////            endpoints.authenticationManager(authenticationManager)
////                    // password模式支持
////                    .userDetailsService(userDetailsService);
////        }
////
////        @Override
////        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
////            clients.inMemory()
////                    .withClient("client_1")
////                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
////                    .scopes("userinfo", "resource")
////                    .resourceIds("web-resource", "api-resource")
////                    .autoApprove(true)
////                    .accessTokenValiditySeconds(1200)
////                    .refreshTokenValiditySeconds(50000)
////                    .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token", "client_credentials")
////                    .redirectUris("http://www.server.com:8000/login/oauth2/code/client",
////                            "http://www.server.com:8000/login/oauth2/code/github",
////                            "http://www.server.com:9020/login/oauth2/code/client",
////                            "http://www.server.com:9020/login/oauth2/code/github",
////                            "https://www.taobao.com",
////                            "https://www.jd.com",
////                            "http://www.client.com:8010/userinfo");
////        }
////    }
//
//    @Configuration
//    @EnableAuthorizationServer //提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
//    public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {
//
//        @Autowired
//        private PasswordEncoder encoder;
//
//        @Autowired
//        private AuthenticationManager authenticationManager;   //认证方式
//
//        @Autowired
//        private UserDetailsService userDetailsService;
//
//        @Autowired
//        private TokenStore tokenStore;
//
//        @Autowired
//        private DefaultTokenServices tokenServices;
//
//        @Override
//        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//            oauthServer
//                    .realm("oauth2-resources")
//                    .tokenKeyAccess("permitAll()") //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
//                    .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
//                    .allowFormAuthenticationForClients();
//        }
//
//        @Override
//        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//            endpoints.tokenStore(tokenStore)
//                    .authenticationManager(authenticationManager)
//                    .userDetailsService(userDetailsService) //必须注入userDetailsService否则根据refresh_token无法加载用户信息
//                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST,HttpMethod.OPTIONS)  //支持GET  POST  请求获取token
//                    .reuseRefreshTokens(true) //开启刷新token
//                    .tokenServices(tokenServices);
//        }
//
//        @Override
//        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            clients.inMemory()
//                    .withClient("client_1")
//                    .secret(encoder.encode("$2a$10$JknlOkbQANofGnc9BRkLv.Kuixt/pZleX2VC54udsy5Gqry7iSFzK"))
//                    .redirectUris("http://www.server.com:8000/login/oauth2/code/client",
//                            "http://www.server.com:8000/login/oauth2/code/github",
//                            "http://www.server.com:9020/login/oauth2/code/client",
//                            "http://www.server.com:9020/login/oauth2/code/github",
//                            "https://www.taobao.com",
//                            "https://www.jd.com",
//                            "http://www.client.com:8010/userinfo")
//                    .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token",
//                            "password", "implicit")
//                    .scopes("userinfo", "resource")
//                    .resourceIds("oauth2-resource")
//                    .autoApprove(true)
//                    .accessTokenValiditySeconds(1200)
//                    .refreshTokenValiditySeconds(50000);
//        }
//    }
//
//    @Configuration
//    @EnableResourceServer
//    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//        @Autowired
//        private DefaultTokenServices tokenServices;
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http.requestMatchers()
////                    .antMatchers("/api/**")
//                    .and()
//                    .authorizeRequests()
////                    .antMatchers("/api/**")
//                    .antMatchers("/api/**")
//                    .fullyAuthenticated();
////                    .and().formLogin().and().httpBasic();
//        }
//
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources) {
//            resources.stateless(true).tokenServices(tokenServices);
//        }
//    }
//
//    @EnableWebSecurity
//    public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        private PasswordEncoder passwordEncoder;
//
//        /**
//         * 1\这里记得设置requestMatchers,不拦截需要token验证的url
//         * 不然会优先被这个filter拦截,走用户端的认证而不是token认证
//         * 2\这里记得对oauth的url进行保护,正常是需要登录态才可以
//         */
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable();
//            http
//                    .requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/oauth/**").authenticated()
//                    .and()
//                    .formLogin();
//        }
//
//        @Bean
//        @Override
//        protected UserDetailsService userDetailsService(){
//            UserDetails userDetails = User.withUsername("user")
//                    .password(passwordEncoder.encode("000"))
//                    .roles("USER", "ADMIN", "CLIENT")
//                    .authorities("resource")
//                    .build();
//            return new InMemoryUserDetailsManager(userDetails);
//        }
//
//        /**
//         * support password grant type
//         * @return
//         * @throws Exception
//         */
//        @Override
//        @Bean
//        public AuthenticationManager authenticationManagerBean() throws Exception {
//            return super.authenticationManagerBean();
//        }
//
//        @Override
//        public void configure(WebSecurity web) {
//            web.debug(true);
//        }
//    }
//
//    /**
//     * 重写默认的资源服务token
//     * @return
//     */
//    @Bean
//    public DefaultTokenServices tokenServices(TokenStore tokenStore) {
//        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
//        return defaultTokenServices;
//    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
//
//}
