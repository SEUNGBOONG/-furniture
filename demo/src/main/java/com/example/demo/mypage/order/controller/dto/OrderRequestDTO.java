package com.example.demo.mypage.order.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDTO {

    private String orderId;
    private int totalAmount;
    private List<OrderItemDTO> products;
}
