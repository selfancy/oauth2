package com.example.oauth2.web.authorization.custom;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;

/**
 * 包装 {@link OAuth2Exception}，使用 {@link CustomOAuth2ExceptionJackson2Serializer} 处理异常返回信息
 *
 * Created by mike on 2019-07-23
 */
@JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
class CustomOAuth2Exception extends OAuth2Exception {

    CustomOAuth2Exception(String msg) {
        super(msg);
    }

    static CustomOAuth2Exception from(OAuth2Exception origin) {
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
