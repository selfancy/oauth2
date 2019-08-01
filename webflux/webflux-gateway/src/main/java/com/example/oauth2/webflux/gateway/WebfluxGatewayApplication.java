package com.example.oauth2.webflux.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
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
 * Created by mike on 2019/7/15
 */
@RestController
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.gateway",
        "com.example.oauth2.common"})
public class WebfluxGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxGatewayApplication.class, args);
    }

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public WebfluxGatewayApplication(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @RequestMapping("/")
    public String index() {
        return "<center>" +
                "<h1>Hello OAuth Server!</h1>" +
                "<br>" +
                "<h2><a href='/github'>Github</a></h2>" +
                "<h2><a href='/client' target='_blank'>OAuth Client</a></h2>" +
                "<h2><a href='/userinfo' target='_blank'>Authorized User</a></h2>" +
                "<h2><a href='/logout'>logout</a></h2>" +
                "</center>";
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
//        Mono<OAuth2AuthorizedClient> authorizedClient = authorizedClientService
//                .loadAuthorizedClient(
//                        authenticationToken.map(OAuth2AuthenticationToken::getAuthorizedClientRegistrationId).block(),
//                        authenticationToken.map(OAuth2AuthenticationToken::getPrincipal).map(AuthenticatedPrincipal::getName).block());
        Map<String, Object> map = new HashMap<>(2);
        map.put("oauth2User", oauth2User);
        map.put("authenticationToken", authenticationToken);
        return Mono.justOrEmpty(map);
    }
}
