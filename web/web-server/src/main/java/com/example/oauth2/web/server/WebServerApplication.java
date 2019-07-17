package com.example.oauth2.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class WebServerApplication {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public WebServerApplication(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
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
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getPrincipal().getName());
        Map<String, Object> objectMap = new HashMap<>(2);
        objectMap.put("oauth2User", oauth2User);
        objectMap.put("authorizedClient", authorizedClient);
        return objectMap;
    }
}
