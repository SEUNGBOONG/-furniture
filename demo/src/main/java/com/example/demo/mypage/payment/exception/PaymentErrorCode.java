package com.example.demo.mypage.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode {

    NOT_FOUND_PAYMENT_BY_ORDER_ID(HttpStatus.NOT_FOUND, "P001", "해당 주문번호의 결제 내역이 없습니다."),
    PAYMENT_CONFIRMATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "P002", "결제 승인에 실패했습니다."),
    PAYMENT_CANCELLATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "P003", "결제 취소에 실패했습니다."),
    INVALID_PAYMENT_REQUEST(HttpStatus.BAD_REQUEST, "P004", "결제 요청 정보가 잘못되었습니다."),
    DUPLICATED_PAYMENT(HttpStatus.CONFLICT, "P005", "이미 처리된 결제입니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "P006", "결제 금액이 주문 금액과 일치하지 않습니다."); // ✅ 추가

    private final HttpStatus httpStatus;
    private final String customCode;
    private final String message;

    PaymentErrorCode(HttpStatus httpStatus, String customCode, String message) {
        this.httpStatus = httpStatus;
        this.customCode = customCode;
        this.message = message;
    }
}
