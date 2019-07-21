package com.example.oauth2.webflux.client;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;

/**
 * Created by mike on 2019-07-20
 */
@Configuration
public class JwtConfig {

    @Bean
    ReactiveJwtDecoder jwtDecoder(@Value("classpath:public.cert") Resource resource) {
        String encodeBase64 = "";
        try {
            encodeBase64 = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NimbusReactiveJwtDecoder jwtDecoder = new NimbusReactiveJwtDecoder(getPublicKey(encodeBase64));
        OAuth2TokenValidator<Jwt> tokenValidator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(60)));
        jwtDecoder.setJwtValidator(tokenValidator);
        return jwtDecoder;
    }

    private static RSAPublicKey getPublicKey(String encodeBase64) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(encodeBase64));
        RSAPublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
