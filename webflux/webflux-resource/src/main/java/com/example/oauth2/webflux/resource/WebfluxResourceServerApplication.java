package com.example.oauth2.webflux.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by mike on 2019-08-01
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.resource",
        "com.example.oauth2.common"})
public class WebfluxResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxResourceServerApplication.class, args);
    }
}
