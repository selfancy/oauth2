package com.example.oauth2.web.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 资源服务器
 *
 * Created by mike on 2019-07-30
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.oauth2.web.resource",
        "com.example.oauth2.common"})
public class WebResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebResourceServerApplication.class, args);
    }
}
