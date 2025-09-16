package com.example.demo.login.global.exception.exceptionhandler;

import com.example.demo.login.global.exception.exceptionhandler.dto.ErrorResponse;
import com.example.demo.login.global.exception.exceptions.CustomErrorCode;
import com.example.demo.login.global.exception.exceptions.CustomException;
import com.example.demo.mypage.payment.exception.PaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        CustomErrorCode customErrorCode = e.getCustomErrorCode();
        ErrorResponse errorResponse =
                new ErrorResponse(customErrorCode.getCustomCode(), customErrorCode.getMessage());
        return ResponseEntity.status(customErrorCode.getHttpStatus()).body(errorResponse);
    }

    // ✅ PaymentException 처리 추가
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentException(PaymentException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", e.getErrorCode().getCustomCode());
        body.put("message", e.getErrorCode().getMessage());

        // Toss 원본 응답이 있으면 내려주기
        if (e.getTossCode() != null) {
            body.put("tossCode", e.getTossCode());
        }
        if (e.getTossMessage() != null) {
            body.put("tossMessage", e.getTossMessage());
        }

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(body);
    }
}
