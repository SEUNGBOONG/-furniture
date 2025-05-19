package com.example.demo.mypage.carItem.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
    private int totalPrice;
    private String image;
}
