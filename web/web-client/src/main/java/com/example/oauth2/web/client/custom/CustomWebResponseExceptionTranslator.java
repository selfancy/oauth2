package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.example.oauth2.web.client.i18n.SecurityExceptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * Created by mike on 2019-07-27
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<Response> {

    private final WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new CustomOAuth2ExceptionWebResponseExceptionTranslator();

    @Override
    public ResponseEntity<Response> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> originResponse = exceptionTranslator.translate(e);
        return translateFromOAuth2Exception(originResponse);
    }

    private ResponseEntity<Response> translateFromOAuth2Exception(ResponseEntity<OAuth2Exception> originResponse) {
        OAuth2Exception oAuth2Exception = originResponse.getBody();
        String message = SecurityExceptionUtil.getMessage(oAuth2Exception);
        return ResponseEntity.status(originResponse.getStatusCode())
                .headers(originResponse.getHeaders())
                .body(Response.fail(oAuth2Exception.getOAuth2ErrorCode(), message));
    }
}
