// com.example.demo.mypage.order.controller.dto.OrderResponse.java
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

    private boolean shipped; // ✅ 배송 여부 추가

    private List<OrderItemResponse> items;
}
