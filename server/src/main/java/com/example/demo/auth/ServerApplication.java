package com.example.demo.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @RequestMapping
    public String index() {
        return "<center>" +
                "<h1>Hello World!</h1>" +
                "<br>" +
                "<h2>Login in <a href='/github'>Github</a>.</h2>" +
                "<h2>Login in <a href='/custom'>Custom</a>.</h2>" +
                "</center>";
    }

    @GetMapping("/github")
    public Map<String, Object> github(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
                                      @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> userDetails = new HashMap<>(2);
        userDetails.put("authorizedClient", authorizedClient);
        userDetails.put("oauth2User", oauth2User);
        return userDetails;
    }

    @GetMapping("/custom")
    public Map<String, Object> custom(@RegisteredOAuth2AuthorizedClient("custom") OAuth2AuthorizedClient authorizedClient,
                                      @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> userDetails = new HashMap<>(2);
        userDetails.put("authorizedClient", authorizedClient);
        userDetails.put("oauth2User", oauth2User);
        return userDetails;
    }

}
