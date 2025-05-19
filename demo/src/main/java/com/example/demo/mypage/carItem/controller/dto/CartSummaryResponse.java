package com.example.demo.mypage.carItem.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartSummaryResponse {
    private List<CartItemResponse> items;
    private int totalAmount;
}
