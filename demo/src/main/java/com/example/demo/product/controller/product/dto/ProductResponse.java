package com.example.demo.product.controller.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String categoryName;
    private String tagName;
    private String image;
}

