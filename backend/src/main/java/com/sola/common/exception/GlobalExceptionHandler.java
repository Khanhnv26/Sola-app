package com.sola.common.exception;

import com.sola.common.dto.ApiResponse;
import com.sola.common.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.Response;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiResponse<Void> response = ApiResponse.error(ErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        ApiResponse<Void> response = ApiResponse.error(ErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        String message = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR, message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = "Tham số '" + ex.getName() + "' có giá trị '" + ex.getValue() + "' không hợp lệ. Vui lòng cung cấp giá trị hợp lệ.";

        ApiResponse<Void> response = ApiResponse.error(ErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(ErrorCode.NOT_FOUND.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.INTERNAL_ERROR, "Lỗi hệ thống. Vui lòng thử lại sau.");
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getHttpStatus()).body(response);
    }
}

