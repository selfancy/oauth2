package com.example.oauth2.webflux.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by mike on 2019/7/15
 */
@EnableEurekaClient
@SpringBootApplication(
        scanBasePackages = {
                "com.example.oauth2.webflux.gateway",
                "com.example.oauth2.common"})
public class WebfluxGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxGatewayApplication.class, args);
    }
}
