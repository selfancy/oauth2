package com.example.oauth2.web.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 授权服务器
 */
@RestController
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.web.authorization",
        "com.example.oauth2.common"})
public class WebAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAuthorizationServerApplication.class, args);
    }

    @GetMapping("/userinfo")
    public Principal user(Principal user) {
        return user;
    }
}
