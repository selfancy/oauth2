package com.example.oauth2.web.authorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
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
 * 生成jks：keytool -genkeypair -alias jwt -keyalg RSA -keypass ngojt2dx8mcI546j -keystore app.jks -storepass 123456
 *
 * 导出公钥：keytool -list -rfc --keystore app.jks | openssl x509 -inform pem -pubkey > public.cert
 *
 * keypass：ngojt2dx8mcI546j
 * storepass：123456
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

    @Bean
    public KeyPair keyPair() {
        Resource keyStore = new ClassPathResource("app.jks");
        char[] keyStorePassword = "123456".toCharArray();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore, keyStorePassword);

        String keyAlias = "jwt";
        char[] keyPassword = "ngojt2dx8mcI546j".toCharArray();
        return keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(KeyPair keyPair) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
        return new JwtTokenStore(accessTokenConverter);
    }

    @Bean
    public TokenEnhancer tokenEnhancer(JwtAccessTokenConverter accessTokenConverter) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(new ExtJwtTokenEnhancer(), accessTokenConverter));
        return enhancerChain;
    }

    @Bean
    public DefaultTokenServices jwtTokenServices(TokenStore tokenStore) {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore);
        return services;
    }

//    @Configuration
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
