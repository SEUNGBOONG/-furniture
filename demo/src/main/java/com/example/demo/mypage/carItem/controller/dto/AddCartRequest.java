package com.example.demo.mypage.carItem.controller.dto;

import lombok.Getter;

@Getter
public class AddCartRequest {
    private Long productId;
    private int quantity;

}
