package com.example.demo.mypage.payment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequestDTO {
    private String orderId;       // ✅ orderId만 받도록
    private String cancelReason;
}
