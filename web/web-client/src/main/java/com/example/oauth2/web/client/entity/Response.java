package com.example.oauth2.web.client.entity;

/**
 * Created by mike on 2019-07-23
 */
public class Response<T> {

    private boolean success;

    private String errorCode;

    private String message;

    private T data;

    public Response() {
    }

    private Response(boolean success, String errorCode, String message, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, "success", "请求成功", data);
    }

    public static <T> Response<T> fail(String errorCode, String message) {
        return new Response<>(false, errorCode, message, null);
    }

    public static <T> Response<T> fail(String message) {
        return fail("fail", message);
    }


}
