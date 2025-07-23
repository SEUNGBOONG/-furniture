package com.example.demo.mypage.payment.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossApproveRequest {
    private String paymentKey;
    private String orderId;
    private int amount;
}
