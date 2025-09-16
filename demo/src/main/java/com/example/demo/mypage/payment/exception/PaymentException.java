package com.example.demo.mypage.payment.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

    private final PaymentErrorCode errorCode;
    private final String tossCode;    // ✅ Toss 원본 에러코드
    private final String tossMessage; // ✅ Toss 원본 메시지

    public PaymentException(PaymentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.tossCode = null;
        this.tossMessage = null;
    }

    public PaymentException(PaymentErrorCode errorCode, String tossCode, String tossMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.tossCode = tossCode;
        this.tossMessage = tossMessage;
    }
}
