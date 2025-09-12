package com.example.demo.mypage.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private int totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;

    private String memberName;
    private String phoneNumber;
    private String roadAddress;
    private String jibunAddress;
    private String zipCode;

    private boolean shipped;

    private String paymentMethod;        // ✅ 결제 수단 (CARD, VIRTUAL_ACCOUNT 등)
    private String virtualAccountNumber; // ✅ 가상계좌 번호
    private String virtualBankCode;      // ✅ 은행 코드
    private LocalDateTime virtualDueDate;// ✅ 입금 기한
    private boolean cashReceiptIssued;   // ✅ 현금영수증 발급 여부

    private List<OrderItemResponse> items;
}
