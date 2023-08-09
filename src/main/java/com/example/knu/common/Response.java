package com.example.knu.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<T> {

    private final ResultCode resultCode;
    private final T data;

    public Response(ResultCode resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ResultCode.SUCCESS, data);
    }

    public static <T> Response<T> unauthorized(T data) {
        return new Response<>(ResultCode.UNAUTHORIZED, data);
    }

    public static <T> Response<T> forbidden(T data) {
        return new Response<>(ResultCode.FORBIDDEN, data);
    }
}
