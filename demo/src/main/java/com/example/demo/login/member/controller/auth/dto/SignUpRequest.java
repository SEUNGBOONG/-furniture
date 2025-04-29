package com.example.demo.login.member.controller.auth.dto;

public record SignUpRequest(
        String memberEmail,
        String memberName,
        String memberPassword,
        String memberNickName
) {
}
