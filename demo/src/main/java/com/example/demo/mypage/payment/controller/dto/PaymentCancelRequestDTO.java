package com.example.demo.mypage.payment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequestDTO {
    private String paymentKey;
    private String cancelReason;
}
