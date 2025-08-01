package com.example.demo.mypage.order.controller.dto;

import lombok.Getter;

@Getter
public class OrderItemDTO {
    private String productId;
    private String productName;
    private String size;
    private int unitPrice;
    private int quantity;
    private int totalPrice;
}
