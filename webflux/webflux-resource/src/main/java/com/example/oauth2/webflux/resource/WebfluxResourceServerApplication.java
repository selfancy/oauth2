package com.example.oauth2.webflux.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by mike on 2019-08-01
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.webflux.resource",
        "com.example.oauth2.common"})
public class WebfluxResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxResourceServerApplication.class, args);
    }
}
