
package com.example.demo.mypage.carItem.controller.dto;

import lombok.Getter;

@Getter
public class AddCartRequest {
    private Long productDetailId;
    private int quantity;
}
