package com.sola.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String code;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .status(ErrorCode.SUCCESS.getHttpStatus().value())
                .code(ErrorCode.SUCCESS.name())
                .message(ErrorCode.SUCCESS.getDefaultMessage())
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        String resolvedMessage = (message == null || message.isBlank())
                ? errorCode.getDefaultMessage()
                : message;
        return ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(resolvedMessage)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }
}
