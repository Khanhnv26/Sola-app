package com.sola.common.exception;

import com.sola.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public AppException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;

    }
}
