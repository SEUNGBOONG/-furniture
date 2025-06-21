package com.example.demo.info.controller.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CompanyHistoryImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imageUrl1;
    private String imageUrl2;

    public CompanyHistoryImageDTO(String imageUrl1, String imageUrl2) {
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
    }

}
