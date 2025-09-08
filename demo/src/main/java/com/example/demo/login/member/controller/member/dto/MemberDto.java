package com.example.demo.login.member.controller.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private String memberName;
    private String roadAddress;
    private String jibunAddress;
    private String zipCode;
    private String phoneNumber;
}
