package com.example.oauth2.web.client.custom;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;

/**
 * Created by mike on 2019-07-23
 */
@JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

    public CustomOAuth2Exception(String msg) {
        super(msg);
    }

    public static CustomOAuth2Exception from(OAuth2Exception origin) {
        CustomOAuth2Exception ex = new CustomOAuth2Exception(origin.getMessage()) {
            @Override
            public String getOAuth2ErrorCode() {
                return origin.getOAuth2ErrorCode();
            }

            @Override
            public int getHttpErrorCode() {
                return origin.getHttpErrorCode();
            }

            @Override
            public Map<String, String> getAdditionalInformation() {
                return origin.getAdditionalInformation();
            }

            @Override
            public String toString() {
                return this.getSummary();
            }

            @Override
            public String getSummary() {
                return origin.getSummary();
            }
        };
        ex.initCause(origin);
        return ex;
    }
}
