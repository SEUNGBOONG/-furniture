package com.example.demo.info.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyDetailDTO {
    private String companyName;
    private String ceo;
    private String establishmentDate;
    private String businessArea;
    private String scale;
    private String mainClient;
}
