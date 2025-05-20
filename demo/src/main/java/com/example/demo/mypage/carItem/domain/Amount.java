package com.example.demo.mypage.carItem.domain;

import com.example.demo.mypage.carItem.controller.dto.CartItemResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class Amount {

    private final int totalAmount;

    private Amount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static Amount calculateTotalAmount(List<CartItemResponse> items) {
        int sum = items.stream()
                .mapToInt(CartItemResponse::getTotalPrice)
                .sum();
        return new Amount(sum);
    }

}
