package com.example.demo.product.controller.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ProductResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private int price;
    private String categoryName;
    private String tagName;
    private String image;
}

