package com.example.demo.login.member.controller.auth.dto;

import com.example.demo.login.member.domain.member.Member;

public record MemberAdminResponse(
        Long id,
        String memberEmail,
        String memberName,
        String memberNickName,
        String phoneNumber,
        boolean checkCorporation,
        String corporationNumber,
        String corporationImageURL
) {
    public static MemberAdminResponse from(Member member) {
        return new MemberAdminResponse(
                member.getId(),
                member.getMemberEmail(),
                member.getMemberName(),
                member.getMemberNickName(),
                member.getPhoneNumber(),  // 전화번호 필드가 있다고 가정
                member.isCheckCorporation(),
                member.getCorporationNumber(),
                member.getCorporationImageURL()
        );
    }
}
