package com.example.demo.product.controller.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String categoryName;
    private String tagName;
    private String image;     // 대표 이미지
    private List<String> images; // 썸네일들
    private List<String> detailImages; // 상세페이지 이미지
    private boolean isLiked;
}
