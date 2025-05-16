package com.example.demo.login.address.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class AddressApiResponse {
    private Results results;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results {
        private List<Juso> juso;


    }


    @Data
    public static class Juso {
        private String roadAddr;
        private String jibunAddr;
        private String zipNo;
    }
}
