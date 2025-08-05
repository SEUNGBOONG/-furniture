package com.example.demo.info.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class CompanyDetailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String companyName;
    private String ceo;
    private String establishmentDate;
    private String businessArea;
    private String scale;
    private String mainClient;
}
