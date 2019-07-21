//package com.example.oauth2.webflux.server;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by mike on 2019-07-19
// */
//@RestController
//public class ServerClientController {
//
//    @GetMapping("/client")
//    public Mono<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authenticationToken) {
////        Mono<OAuth2AuthorizedClient> authorizedClient = authorizedClientService
////                .loadAuthorizedClient(
////                        authenticationToken.map(OAuth2AuthenticationToken::getAuthorizedClientRegistrationId).block(),
////                        authenticationToken.map(OAuth2AuthenticationToken::getPrincipal).map(AuthenticatedPrincipal::getName).block());
//        Map<String, Object> map = new HashMap<>(2);
//        map.put("oauth2User", oauth2User);
//        map.put("authenticationToken", authenticationToken);
//        return Mono.justOrEmpty(map);
//    }
//}
