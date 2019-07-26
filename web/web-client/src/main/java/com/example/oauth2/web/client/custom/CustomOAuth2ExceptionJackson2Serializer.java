package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by mike on 2019-07-23
 */
public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<CustomOAuth2Exception> {

    protected CustomOAuth2ExceptionJackson2Serializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception e, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        String oAuth2ErrorCode = e.getOAuth2ErrorCode();
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            errorMessage = HtmlUtils.htmlEscape(errorMessage);
        }
        Response<?> response = Response.fail(oAuth2ErrorCode, errorMessage);
        jgen.writeObject(response);
    }
}
