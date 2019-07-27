package com.example.oauth2.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Locale;

/**
 * Created by mike on 2019/7/12
 */
@RestController
@SpringBootApplication
public class WebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "<center>" +
                "<h1>Hello OAuth Client!</h1>" +
                "</center>";
    }

    @GetMapping("/userinfo")
    public Principal userInfo(Principal principal) {
        return principal;
    }

    @RequestMapping("/admin")
    public String admin() {
        return "<center>" +
                "<h1>Hello Admin!</h1>" +
                "</center>";
    }

    @RequestMapping("/resource")
    public String resource() {
        return "<center>" +
                "<h1>Hello Resource!</h1>" +
                "</center>";
    }

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/i18n")
    public String i18n() {
        return messageSource.getMessage("SecurityExceptionUtil.badCredentials", null, LocaleContextHolder.getLocale());
    }
}
