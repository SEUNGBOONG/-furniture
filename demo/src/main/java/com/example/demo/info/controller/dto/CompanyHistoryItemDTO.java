package com.example.demo.info.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyHistoryItemDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl1;
    private String imageUrl2;
}
