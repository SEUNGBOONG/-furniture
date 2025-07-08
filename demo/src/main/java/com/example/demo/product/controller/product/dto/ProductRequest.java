package com.example.demo.product.controller.product.dto;

import lombok.Getter;

@Getter
public class ProductRequest {
    private String name;
    private String description;
    private Long categoryId;
    private int price;
    private String tag;
}
