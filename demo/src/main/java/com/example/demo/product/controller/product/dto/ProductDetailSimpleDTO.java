package com.example.demo.product.controller.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ProductDetailSimpleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;       // ✅ detail id
    private String model;
    private String size;
    private int price;     // ✅ price 추가
    private boolean isLiked; // ✅ 추가
}
