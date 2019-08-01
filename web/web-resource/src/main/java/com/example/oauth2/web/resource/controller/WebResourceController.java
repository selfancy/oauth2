package com.example.oauth2.web.resource.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

/**
 * Created by mike on 2019-07-30
 */
@RestController
public class WebResourceController {

    @RequestMapping("/common")
    public String common() {
        return "<center>" +
                "<h1>Common Resource!</h1>" +
                "</center>";
    }

    @RequestMapping("/resource")
    public String resource() {
        return "<center>" +
                "<h1>Secured Web Resource!</h1>" +
                "</center>";
    }

    @RequestMapping("/api")
    public JSONObject api(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        json.put("time", new Date());
        json.put("ip", request.getRemoteAddr());
        return json;
    }

    @RequestMapping("/userinfo")
    public Principal userinfo(Principal principal) {
        return principal;
    }
}
