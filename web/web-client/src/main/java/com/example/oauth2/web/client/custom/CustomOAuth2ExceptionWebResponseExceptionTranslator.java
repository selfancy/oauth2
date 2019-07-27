package com.example.oauth2.web.client.custom;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

/**
 * Created by mike on 2019-07-25
 */
public class CustomOAuth2ExceptionWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
        OAuth2Exception originEx = responseEntity.getBody();
        HttpHeaders headers = responseEntity.getHeaders();
        return new ResponseEntity<>(CustomOAuth2Exception.from(originEx), headers, responseEntity.getStatusCode());
    }
}
