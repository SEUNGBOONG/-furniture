package com.example.demo.login.member.controller.auth.dto;

public record LoginResponse(
        String token,
        Long memberId,
        String memberName,
        String memberNickName
) {
}
