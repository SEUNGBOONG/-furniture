package com.example.demo.mypage.carItem.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartItemDeleteRequest {
    private List<Long> productIds;
}
