package com.example.demo.payment.toss.controller.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private int amount;
    private String orderId;
    private String orderName;
    private String customerName;

    private String successUrl;  // 추가
    private String failUrl;     // 추가

    // getter, setter 생략
}

