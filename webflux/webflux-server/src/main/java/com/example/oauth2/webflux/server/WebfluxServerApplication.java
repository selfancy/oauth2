package com.example.oauth2.webflux.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by mike on 2019-07-19
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.server",
        "com.example.oauth2.common"})
public class WebfluxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxServerApplication.class, args);
    }
}
