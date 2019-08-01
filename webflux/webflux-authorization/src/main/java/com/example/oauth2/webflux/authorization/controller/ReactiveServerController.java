package com.example.oauth2.webflux.authorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * Created by mike on 2019-07-19
 */
@RestController
public class ReactiveServerController {

    @GetMapping("/userinfo")
    public Mono<Principal> userInfo(Principal principal) {
        return Mono.justOrEmpty(principal);
    }
}
