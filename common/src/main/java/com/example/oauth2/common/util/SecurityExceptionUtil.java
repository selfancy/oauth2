package com.example.oauth2.common.util;

import com.example.oauth2.common.entity.Response;
import com.example.oauth2.common.i18n.LocaleUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Locale;

/**
 * 异常处理
 * <p>
 * Created by mike on 2019-07-25
 */
public class SecurityExceptionUtil implements MessageSourceAware {

    private static MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        SecurityExceptionUtil.messageSource = messageSource;
    }

    public static Response<String> getErrorResponse(Throwable e) {
        Locale locale = LocaleUtil.resolveLocale();
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        String errorCode = "fail";
        String errorMessage = rootCause.getMessage();
        if (rootCause instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) rootCause;
            String resourceKey = OAuth2Exception.class.getSimpleName() + '.' + oAuth2Exception.getOAuth2ErrorCode();
            if (errorMessage != null) {
                errorMessage = HtmlUtils.htmlEscape(errorMessage);
            }
            errorCode = oAuth2Exception.getOAuth2ErrorCode();
            errorMessage = messageSource.getMessage(resourceKey, null, errorMessage, locale);
        }
        if (rootCause instanceof AuthenticationException) {
            String resourceKey = AuthenticationException.class.getSimpleName() + '.' + getSimpleNamePrefix(rootCause.getClass());
            errorCode = "unauthorized";
            errorMessage = messageSource.getMessage(resourceKey, null, errorMessage, locale);
        }
        if (rootCause instanceof AccessDeniedException) {
            errorCode = "forbidden";
            errorMessage = messageSource.getMessage("AccessDeniedException.accessDenied", null, errorMessage, locale);
        }
        return Response.fail(errorCode, errorMessage);
    }

    private static String getSimpleNamePrefix(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        String simpleNamePrefix = simpleName.substring(0, simpleName.indexOf(Exception.class.getSimpleName()));
        return StringUtils.uncapitalize(simpleNamePrefix);
    }
}
