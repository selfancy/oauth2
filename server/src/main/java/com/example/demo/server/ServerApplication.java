package com.example.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "<center>" +
                "<h1>Hello OAuth Server!</h1>" +
                "<br>" +
                "<h2><a href='/github'>Github</a></h2>" +
                "<h2><a href='/client' target='_blank'>Custom Client</a></h2>" +
                "<h2><a href='/userinfo' target='_blank'>Authorized User</a></h2>" +
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
    public OAuth2User userInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }
}
