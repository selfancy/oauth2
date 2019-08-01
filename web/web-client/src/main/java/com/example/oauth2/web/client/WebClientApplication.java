package com.example.oauth2.web.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 客户端
 *
 * Created by mike on 2019/7/12
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.web.client",
        "com.example.oauth2.common"})
public class WebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }
}
