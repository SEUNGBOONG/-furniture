package com.example.demo.login.member.controller.auth.dto;

public record NormalSignUpRequest(
        String memberEmail,
        String memberName,
        String memberPassword,
        String memberNickName,
        String roadAddress,
        String jibunAddress,
        String zipCode,
        String phoneNumber
) {
}
