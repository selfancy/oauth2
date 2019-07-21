package com.example.oauth2.webflux.client;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2019-07-19
 */
@RestController
public class ClientResourceController {

    @GetMapping("/client")
    public OAuth2AuthorizedClient custom(@RegisteredOAuth2AuthorizedClient("client") OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient;
    }

    @GetMapping("/jwt")
    public Jwt jwt(@AuthenticationPrincipal Jwt jwt) {
        return jwt;
    }

    @GetMapping("/resource")
    public Mono<Map<String, Object>> resource(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authenticationToken) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("oauth2User", oauth2User);
        map.put("authenticationToken", authenticationToken);
        return Mono.justOrEmpty(map);
    }
}
