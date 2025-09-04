package com.example.demo.mypage.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemResponse {
    private String productId;
    private String productName;
    private String size;
    private int unitPrice;
    private int quantity;
    private int totalPrice;
    private String productImage; // ✅ 이미지 추가 (원하면 제거 가능)
}
