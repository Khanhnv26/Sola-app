package com.sola.common;

import java.time.Instant;

public record ApiResponse<T>(
        int status,
        String code,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS.getHttpStatus().value(),
                ErrorCode.SUCCESS.name(),
                ErrorCode.SUCCESS.getDefaultMessage(),
                data,
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        String resolvedMessage = (message == null || message.isBlank())
                ? errorCode.getDefaultMessage()
                : message;
        return new ApiResponse<>(
                errorCode.getHttpStatus().value(),
                errorCode.name(),
                resolvedMessage,
                null,
                Instant.now()
        );
    }
}
