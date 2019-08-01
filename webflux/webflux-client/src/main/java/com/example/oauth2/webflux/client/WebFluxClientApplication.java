package com.example.oauth2.webflux.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by mike on 2019/7/15
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.client",
        "com.example.oauth2.common"})
public class WebFluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxClientApplication.class, args);
    }
}
