package com.example.oauth2.web.client.custom;

import com.example.oauth2.web.client.entity.Response;
import com.example.oauth2.web.client.i18n.SecurityExceptionUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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
        Response<String> errorResponse = SecurityExceptionUtil.getErrorResponse(e);
        jgen.writeObject(errorResponse);
    }
}
