//package com.example.oauth2.web.client.sso;
//
//import com.example.oauth2.web.client.util.TokenContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class SimpleCORSFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String token = req.getParameter("access_token");
//        System.out.println(" token -- "+ token);
//        if(!StringUtils.isEmpty(token)){
//            TokenContextHolder.setToken(token);
//        }
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Type", "application/json");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Accept,Authorization");
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) {}
//
//    @Override
//    public void destroy() {}
//
//}