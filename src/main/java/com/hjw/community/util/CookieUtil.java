package com.hjw.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hujw
 * @description
 * @create 2021-10-13 22:07
 */
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name){
        if (request == null || name == null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
