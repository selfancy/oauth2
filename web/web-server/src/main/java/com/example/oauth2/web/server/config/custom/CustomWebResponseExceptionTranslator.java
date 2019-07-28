package com.example.oauth2.web.server.config.custom;

import com.example.oauth2.web.server.entity.Response;
import com.example.oauth2.web.server.i18n.SecurityExceptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 自定义权限认证异常处理
 *
 * Created by mike on 2019-07-27
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<Response> {

    private final WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    @Override
    public ResponseEntity<Response> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> originResponse = exceptionTranslator.translate(e);
        return translateFromOAuth2Exception(originResponse);
    }

    private ResponseEntity<Response> translateFromOAuth2Exception(ResponseEntity<OAuth2Exception> originResponse) {
        OAuth2Exception oAuth2Exception = originResponse.getBody();
        Response<String> errorResponse = SecurityExceptionUtil.getErrorResponse(oAuth2Exception);
        return ResponseEntity.status(originResponse.getStatusCode())
                .headers(originResponse.getHeaders())
                .body(errorResponse);
    }
}
