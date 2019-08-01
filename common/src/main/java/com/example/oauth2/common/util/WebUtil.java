package com.example.oauth2.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mike on 2019-07-26
 */
public class WebUtil {

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxFlag);
    }
}
