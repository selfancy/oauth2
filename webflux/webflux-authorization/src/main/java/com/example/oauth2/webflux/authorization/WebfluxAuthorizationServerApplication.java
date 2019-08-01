package com.example.oauth2.webflux.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by mike on 2019-07-19
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.authorization",
        "com.example.oauth2.common"})
public class WebfluxAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxAuthorizationServerApplication.class, args);
    }
}
