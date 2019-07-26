package com.example.oauth2.web.client.i18n;

import com.example.oauth2.web.client.config.LocaleUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by mike on 2019-07-25
 */
@Component
public class SecurityExceptionUtil implements MessageSourceAware {

    private static MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        SecurityExceptionUtil.messageSource = messageSource;
    }

    public static String getMessage(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        return getMessage(e, LocaleUtil.resolveLocale(request, response));
    }

    public static String getMessage(AuthenticationException e, Locale locale) {
        String errorMessage = e.getMessage();
        // 权限认证不足异常
        if (e instanceof InsufficientAuthenticationException) {
            if (e instanceof AccessTokenRequiredException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.accessTokenRequired", null, locale);
            } else if (e instanceof UnapprovedClientAuthenticationException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.unapprovedClientAuthentication", null, locale);
            } else {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.insufficientAuthentication", null, locale);
            }
        }
        // 账号状态异常
        if (e instanceof AccountStatusException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.accountStatus", null, locale);
            if (e instanceof AccountExpiredException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.accountExpired", null, locale);
            }
            if (e instanceof CredentialsExpiredException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.credentialsExpired", null, locale);
            }
            if (e instanceof DisabledException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.disabled", null, locale);
            }
            if (e instanceof LockedException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.locked", null, locale);
            }
        }
        // 认证服务异常
        if (e instanceof AuthenticationServiceException) {
            if (e instanceof InternalAuthenticationServiceException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.internalAuthenticationService", null, locale);
            } else {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.authenticationService", null, locale);
            }
        }
        // 记住我认证异常
        if (e instanceof RememberMeAuthenticationException) {
            if (e instanceof CookieTheftException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.cookieTheft", null, locale);
            } else if (e instanceof InvalidCookieException) {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.invalidCookie", null, locale);
            } else {
                errorMessage = messageSource.getMessage("SecurityExceptionUtil.rememberMeAuthentication", null, locale);
            }
        }
        if (e instanceof BadCredentialsException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.badCredentials", null, locale);
        }
        if (e instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.authenticationCredentialsNotFound", null, locale);
        }
        if (e instanceof NonceExpiredException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.nonceExpired", null, locale);
        }
        if (e instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2Ex = (OAuth2AuthenticationException) e;
            errorMessage = oAuth2Ex.getError().getDescription();
        }
        if (e instanceof PreAuthenticatedCredentialsNotFoundException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.preAuthenticatedCredentialsNotFound", null, locale);
        }
        if (e instanceof ProviderNotFoundException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.providerNotFound", null, locale);
        }
        if (e instanceof SessionAuthenticationException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.sessionAuthentication", null, locale);
        }
        if (e instanceof UsernameNotFoundException) {
            errorMessage = messageSource.getMessage("SecurityExceptionUtil.usernameNotFound", null, locale);
        }
        return errorMessage;
    }

    public static String getMessage(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = LocaleUtil.resolveLocale(request, response);
        return messageSource.getMessage("SecurityExceptionUtil.accessDenied", null, e.getMessage(), locale);
    }
}
