package com.example.demo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by mike on 2019/7/12
 */
@RestController
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @RequestMapping
    public String index() {
        return "<center>" +
                    "<h1>Hello OAuth Client!</h1>" +
                "</center>";
    }

    @GetMapping("/user/me")
    public Principal userInfo(Principal principal) {
        return principal;
    }
}
