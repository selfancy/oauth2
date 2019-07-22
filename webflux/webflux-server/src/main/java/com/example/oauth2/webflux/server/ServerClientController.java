package com.example.oauth2.webflux.server;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
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
public class ServerClientController {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public ServerClientController(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/github")
    public OAuth2AuthorizedClient github(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient;
    }

    @GetMapping("/client")
    public OAuth2AuthorizedClient custom(@RegisteredOAuth2AuthorizedClient("client") OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient;
    }

    @GetMapping("/userinfo")
    public Mono<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authenticationToken) {
        Mono<OAuth2AuthorizedClient> authorizedClient = authorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getPrincipal().getName());
        Map<String, Object> objectMap = new HashMap<>(2);
        objectMap.put("oauth2User", oauth2User);
        objectMap.put("authorizedClient", authorizedClient);
        return Mono.justOrEmpty(objectMap);
    }
}
