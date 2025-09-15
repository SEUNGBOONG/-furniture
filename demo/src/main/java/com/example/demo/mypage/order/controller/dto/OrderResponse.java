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
    private String phoneNumber;   // ✅ 이미 있음
    private String roadAddress;
    private String jibunAddress;
    private String zipCode;

    private boolean shipped;

    private String paymentMethod;
    private String virtualAccountNumber;
    private String virtualBankCode;
    private LocalDateTime virtualDueDate;
    private boolean cashReceiptIssued;

    private List<OrderItemResponse> items;
}
