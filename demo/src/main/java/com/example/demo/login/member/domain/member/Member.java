package com.example.demo.login.member.domain.member;

import com.example.demo.login.member.exception.exceptions.auth.NotSamePasswordException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberNickName;

    @Column(nullable = false)
    private String roadAddress;

    @Column(nullable = false)
    private String jibunAddress;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String phoneNumber;

    private boolean checkCorporation;

    private String corporationNumber;

    private String corporationImageURL;

    public void updatePassword(String newPassword) {
        this.memberPassword = newPassword;
    }

}
