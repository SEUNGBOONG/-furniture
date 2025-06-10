package com.example.demo.login.member.controller.auth.dto;

import lombok.Getter;

@Getter
public class AuthRequestDTO {
    private String email;
    private String authCode;

}
