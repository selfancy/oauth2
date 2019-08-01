package com.example.oauth2.common.i18n;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by mike on 2019-07-26
 */
@Component
public class LocaleUtil {

    private static LocaleChangeInterceptor localeChangeInterceptor;

    private static LocaleResolver localeResolver;

    public LocaleUtil(@Lazy LocaleChangeInterceptor localeChangeInterceptor,
                      @Lazy LocaleResolver localeResolver) {
        LocaleUtil.localeChangeInterceptor = localeChangeInterceptor;
        LocaleUtil.localeResolver = localeResolver;
    }

    public static Locale resolveLocale() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        return resolveLocale(request, response);
    }

    public static Locale resolveLocale(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = null;
        String newLocale = request.getParameter(localeChangeInterceptor.getParamName());
        if (newLocale != null) {
            locale = StringUtils.parseLocale(newLocale);
        }
        if (hasNoneLocale(locale)) {
            locale = request.getLocale();
        }
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            localeResolver = LocaleUtil.localeResolver;
        }
        if (hasNoneLocale(locale)) {
            locale = localeResolver.resolveLocale(request);
        }
        if (hasNoneLocale(locale)) {
            locale = LocaleContextHolder.getLocale();
        }
        if (!hasNoneLocale(locale)) {
            localeResolver.setLocale(request, response, locale);
            request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);

            LocaleContextHolder.setLocale(locale);
        }
        return locale;
    }

    private static boolean hasNoneLocale(Locale locale) {
        return locale == null || StringUtils.isEmpty(locale.toString());
    }

}
