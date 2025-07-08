package com.example.demo.product.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TagResponse {

    private Long id;
    private String name;
    private List<String> tagName;

}
