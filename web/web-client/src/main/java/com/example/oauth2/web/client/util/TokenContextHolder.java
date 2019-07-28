package com.example.oauth2.web.client.util;

public class TokenContextHolder {

    private static final ThreadLocal<String> LOCAL_TOKEN = new ThreadLocal<>();

    public static void setToken(String value){
        LOCAL_TOKEN.set(value);
    }

    public static String getToken(){
        String token = LOCAL_TOKEN.get();
        clearToken();
        return token;
    }

    public static void clearToken(){
        LOCAL_TOKEN.remove();
    }
}