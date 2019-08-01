package com.example.oauth2.webflux.resource.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Date;

/**
 * Created by mike on 2019-08-01
 */
@RestController
public class WebfluxResourceController {

    @GetMapping("/common")
    public Mono<String> common() {
        return Mono.just("<center>" +
                "<h1>Common Resource!</h1>" +
                "</center>");
    }

    @GetMapping("/resource")
    public Mono<String> resource() {
        return Mono.just("<center>" +
                "<h1>Secured Web Resource!</h1>" +
                "</center>");
    }

    @GetMapping("/api")
    public Mono<JSONObject> api(ServerHttpRequest request) {
        JSONObject json = new JSONObject();
        json.put("time", new Date());
        json.put("ip", request.getRemoteAddress());
        return Mono.just(json);
    }

    @GetMapping("/userinfo")
    public Mono<Principal> userinfo(Principal principal) {
        return Mono.just(principal);
    }
}
