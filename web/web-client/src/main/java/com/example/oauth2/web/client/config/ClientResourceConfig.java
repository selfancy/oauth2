//package com.example.oauth2.web.client.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
///**
// * Created by mike on 2019-07-21
// */
//@Configuration
//public class ClientResourceConfig {
//
//    @Configuration
//    @EnableResourceServer
//    static class CustomResourceServer extends ResourceServerConfigurerAdapter {
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources) {
//            //此处是关键，默认stateless=true，只支持access_token形式，
//            // OAuth2客户端连接需要使用session，所以需要设置成false以支持session授权
//            resources.stateless(false);
//        }
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests()
//                    .antMatchers("/login").permitAll()
//                    .antMatchers("/userinfo").hasAnyAuthority("SCOPE_userinfo.read")
//                    .antMatchers("/admin").hasRole("ADMIN")
//                    .antMatchers("/dba").hasRole("DBA")
//                    .anyRequest().authenticated()
//                    .and()
//                    .formLogin().and().httpBasic();
//
//            //需要的时候创建session，支持从session中获取认证信息，ResourceServerConfiguration中
//            //session创建策略是stateless不使用，这里其覆盖配置可创建session
//            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//        }
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user1 = User.withUsername("user")
//                .password(passwordEncoder.encode("000"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.withUsername("mike")
//                .password(passwordEncoder.encode("000"))
//                .roles("USER", "ADMIN", "DBA", "CLIENT")
//                .authorities("SCOPE_userinfo.read")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
//
//    @EnableWebSecurity
//    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    }
//}
