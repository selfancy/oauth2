package com.example.oauth2.web.client.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 自定义配置
 * <p>
 * Created by mike on 2019-07-28
 */
@Configuration
public class CustomConfig {

    /**
     * web响应异常转换器
     */
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    /**
     * OAuth2 AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler oAuth2AccessDeniedHandler(WebResponseExceptionTranslator exceptionTranslator) {
        OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
        accessDeniedHandler.setExceptionTranslator(exceptionTranslator);
        return accessDeniedHandler;
    }

    /**
     * OAuth2 AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint oAuth2AuthenticationEntryPoint(WebResponseExceptionTranslator exceptionTranslator) {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(exceptionTranslator);
        return authenticationEntryPoint;
    }
}
