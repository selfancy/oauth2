package com.example.oauth2.web.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Arrays;

/**
 * Jwt 配置
 *
 * Created by mike on 2019-07-30
 */
@Configuration
public class JwtConfig {

//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter(){
//        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
//        accessTokenConverter.setSigningKey("ngojt2dx8mcI546j");
//        return accessTokenConverter;
//    }

    /**
     * jks秘钥配置
     *
     * 生成jks秘钥库：keytool -genkeypair -alias mytest -keyalg RSA -keypass mypass -keystore mytest.jks -storepass mypass
     *
     * 导出公钥：keytool -list -rfc --keystore mytest.jks | openssl x509 -inform pem -pubkey > public.cert
     */
    @Bean
    public KeyPair keyPair() {
        Resource keyStore = new ClassPathResource("mytest.jks");
        char[] keyStorePassword = "mypass".toCharArray();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore, keyStorePassword);

        String keyAlias = "mytest";
        char[] keyPassword = "mypass".toCharArray();
        return keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(KeyPair keyPair) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    /**
     * 配置jwt token存储
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
        return new JwtTokenStore(accessTokenConverter);
    }

    /**
     * jwt token增强, 加入自定义信息
     */
    @Bean
    public TokenEnhancer tokenEnhancer(JwtAccessTokenConverter accessTokenConverter) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        //自定义token增强器, 集合元素位置不同，生成自定义参数位置不同
        enhancerChain.setTokenEnhancers(Arrays.asList(new ExtJwtTokenEnhancer(), accessTokenConverter));
        return enhancerChain;
    }

    @Bean
    public DefaultTokenServices jwtTokenServices(TokenStore tokenStore) {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore);
        return services;
    }

    /**
     * 加入jwk-set-uri，提供给资源服务器
     */
    @Order(-1)  //配置在默认 AuthorizationServerSecurityConfiguration 之前
    @Configuration
    static class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http
                    .requestMatchers()
                    .mvcMatchers("/.well-known/jwks.json")
                    .and()
                    .authorizeRequests()
                    .mvcMatchers("/.well-known/jwks.json").permitAll();
        }
    }
}
