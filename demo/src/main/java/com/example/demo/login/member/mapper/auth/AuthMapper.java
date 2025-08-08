package com.example.demo.login.member.mapper.auth;

import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.NormalSignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;
import com.example.demo.login.member.domain.member.Member;
public class AuthMapper {

    public static Member toMember(SignUpRequest signUpRequest, String encodedPassword, String imageUrl) {
        return Member.builder()
                .memberEmail(signUpRequest.memberEmail())
                .memberName(signUpRequest.memberName())
                .memberPassword(encodedPassword)
                .memberNickName(signUpRequest.memberNickName())
                .roadAddress(signUpRequest.roadAddress())
                .jibunAddress(signUpRequest.jibunAddress())
                .zipCode(signUpRequest.zipCode())
                .checkCorporation(true)
                .corporationNumber(signUpRequest.corporationNumber())
                .corporationImageURL(imageUrl)
                .phoneNumber(signUpRequest.phoneNumber())
                .build();
    }

    public static Member toNormalMember(NormalSignUpRequest request, String encodedPassword) {
        return Member.builder()
                .memberEmail(request.memberEmail())
                .memberName(request.memberName())
                .memberPassword(encodedPassword)
                .memberNickName(request.memberNickName())
                .roadAddress(request.roadAddress())
                .jibunAddress(request.jibunAddress())
                .zipCode(request.zipCode())
                .checkCorporation(false)
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public static SignUpResponse toSignUpResponse(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberNickName()
        );
    }

    public static LoginResponse toLoginResponse(Member member) {
        return new LoginResponse(
                member.getId(),
                member.getMemberName(),
                member.getMemberNickName(),
                member.isCheckCorporation()
        );
    }
}
