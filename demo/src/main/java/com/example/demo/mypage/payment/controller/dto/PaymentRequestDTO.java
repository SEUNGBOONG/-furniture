package com.example.demo.mypage.payment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private String paymentKey;
    private String orderId;
    private int amount;
}
