package com.example.demo.login.member.mapper.auth;

import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;
import com.example.demo.login.member.domain.member.Member;

public class AuthMapper {

    // AuthMapper.java
    public static Member toMember(SignUpRequest signUpRequest, String imageUrl) {
        return Member.builder()
                .memberEmail(signUpRequest.memberEmail())
                .memberName(signUpRequest.memberName())
                .memberPassword(signUpRequest.memberPassword())
                .memberNickName(signUpRequest.memberNickName())
                .roadAddress(signUpRequest.roadAddress())
                .jibunAddress(signUpRequest.jibunAddress())
                .zipCode(signUpRequest.zipCode())
                .checkCorporation(true)
                .corporationNumber(signUpRequest.corporationNumber())
                .corporationImageURL(imageUrl)
                .build();
    }

    public static Member toNormalMember(SignUpRequest request) {
        return Member.builder()
                .memberEmail(request.memberEmail())
                .memberName(request.memberName())
                .memberPassword(request.memberPassword())
                .memberNickName(request.memberNickName())
                .roadAddress(request.roadAddress())
                .jibunAddress(request.jibunAddress())
                .zipCode(request.zipCode())
                .checkCorporation(false)
                .build();
    }

    public static SignUpResponse toSignUpResponse(Member member) {
        return new SignUpResponse(member.getId(), member.getMemberName(), member.getMemberEmail(),
                member.getMemberPassword(), member.getMemberNickName());
    }

    public static LoginResponse toLoginResponse(String token, Member member) {
        return new LoginResponse(
                token,
                member.getId(),
                member.getMemberName(),
                member.getMemberNickName(),
                member.isCheckCorporation()
        );
    }
}
