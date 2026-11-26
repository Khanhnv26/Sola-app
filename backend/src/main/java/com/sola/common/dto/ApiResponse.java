package com.sola.common.dto;


import com.sola.common.enums.ErrorCode;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private int status;
    private String code;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(ErrorCode.SUCCESS.getHttpStatus().value())
                .code(ErrorCode.SUCCESS.name())
                .message(ErrorCode.SUCCESS.getDefaultMessage())
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(message != null && !message.isBlank() ? message : errorCode.getDefaultMessage())
                .data(null)
                .timestamp(Instant.now())
                .build();
    }
}
