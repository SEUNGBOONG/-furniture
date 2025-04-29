package com.example.demo.login.member.controller.dto;

import lombok.Getter;

@Getter
public class AuthRequestDTO {
    private String email;
    private String authCode;

}
