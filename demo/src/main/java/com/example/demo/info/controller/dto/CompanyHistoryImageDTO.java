package com.example.demo.info.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record CompanyHistoryImageDTO(String imageUrl1, String imageUrl2) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
