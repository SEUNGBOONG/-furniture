package com.example.demo.product.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ProductDetailSimpleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String model;
    private String size;
}
