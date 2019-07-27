package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.example.oauth2.web.client.i18n.SecurityExceptionUtil;
import com.example.oauth2.web.client.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义Token异常信息,用于token校验失败返回信息
 * <p>
 * Created by mike on 2019-07-23
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String LOGIN_URL = "/login";

    private final ObjectMapper objectMapper;

//    private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(LOGIN_URL);
    private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

    private final AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ((OAuth2AuthenticationEntryPoint) this.oAuth2AuthenticationEntryPoint).setExceptionTranslator(new CustomWebResponseExceptionTranslator());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (WebUtil.isAjaxRequest(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            String errorMessage = SecurityExceptionUtil.getMessage(e);
            objectMapper.writeValue(response.getOutputStream(),
                    Response.fail("unauthorized", errorMessage));
        } else {
            oAuth2AuthenticationEntryPoint.commence(request, response, e);
//            if (isOAuth2Request(request)) {
//                oAuth2AuthenticationEntryPoint.commence(request, response, e);
//            } else {
//                loginUrlAuthenticationEntryPoint.commence(request, response, e);
//            }
        }
    }

    private boolean isOAuth2Request(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/oauth") ||
                request.getParameter("access_token") != null ||
                request.getHeader("Authorization") != null;
    }
}
