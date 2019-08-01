package com.example.oauth2.web.authorization.endpoint;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Created by mike on 2019-07-31
 */
@FrameworkEndpoint
class JwkSetEndpoint {

    private final KeyPair keyPair;

    public JwkSetEndpoint(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @ResponseBody
    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
