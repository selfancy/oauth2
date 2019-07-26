package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.example.oauth2.web.client.i18n.SecurityExceptionUtil;
import com.example.oauth2.web.client.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义Token异常信息,用于token校验失败返回信息
 *
 * Created by mike on 2019-07-23
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String LOGIN_URL = "/login";

    private final ObjectMapper objectMapper;

    private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(LOGIN_URL);

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
            loginUrlAuthenticationEntryPoint.commence(request, response, e);
        }
    }
}
