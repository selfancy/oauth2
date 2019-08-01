package com.example.oauth2.webflux.client.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2019-07-29
 */
@RestController
public class ReactiveClientController {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public ReactiveClientController(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @RequestMapping("/")
    public Mono<String> index() {
        String indexHtml = "<center>" +
                "<h1>Hello OAuth Client!</h1>" +
                "<br>" +
                "<h2><a href='/github'>Github</a></h2>" +
                "<h2><a href='/web-client' target='_blank'>Web Client</a></h2>" +
                "<h2><a href='/userinfo' target='_blank'>Authorized User</a></h2>" +
                "<h2><a href='/logout'>logout</a></h2>" +
                "</center>";
        return Mono.just(indexHtml);
    }

    @GetMapping("/github")
    public Mono<OAuth2AuthorizedClient> github(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient) {
        return Mono.just(authorizedClient);
    }

    @GetMapping("/web-client")
    public Mono<OAuth2AuthorizedClient> custom(@RegisteredOAuth2AuthorizedClient("web-client") OAuth2AuthorizedClient authorizedClient) {
        return Mono.just(authorizedClient);
    }

    @GetMapping("/userinfo")
    public Mono<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authenticationToken) {
        Mono<OAuth2AuthorizedClient> authorizedClient = authorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getPrincipal().getName());
        Map<String, Object> objectMap = new HashMap<>(2);
        objectMap.put("oauth2User", oauth2User);
        objectMap.put("authorizedClient", authorizedClient.block());
        return Mono.just(objectMap);
    }
}
