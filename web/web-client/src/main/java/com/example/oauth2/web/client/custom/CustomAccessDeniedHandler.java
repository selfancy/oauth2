package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.example.oauth2.web.client.i18n.SecurityExceptionUtil;
import com.example.oauth2.web.client.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权异常处理
 * <p>
 * Created by mike on 2019-07-23
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        if (WebUtil.isAjaxRequest(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            objectMapper.writeValue(response.getOutputStream(),
                    Response.fail("unauthorized", SecurityExceptionUtil.getMessage(e)));
        } else {
            accessDeniedHandler.handle(request, response, e);
        }
    }
}
