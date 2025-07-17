package com.example.demo.login.member.service.auth;

import com.example.demo.config.s3.S3Uploader;

import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.NormalSignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;

import com.example.demo.login.member.domain.auth.EmailValidator;
import com.example.demo.login.member.domain.auth.SignUpValidator;
import com.example.demo.login.member.domain.member.Member;

import com.example.demo.login.member.exception.exceptions.auth.NotSamePasswordException;

import com.example.demo.login.member.infrastructure.auth.JwtTokenProvider;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;

import com.example.demo.login.member.mapper.auth.AuthMapper;

import com.example.demo.login.util.AuthValidator;
import com.example.demo.login.util.CorporationValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final S3Uploader s3Uploader;
    private final PasswordEncoder passwordEncoder;

    private final SignUpValidator signUpValidator;
    private final EmailValidator emailValidator;
    private final CorporationValidator corporationValidator;

    private final AuthValidator authValidator;

    public Member signUp(SignUpRequest signUpRequest, MultipartFile corporationImage) {
        signUpValidator.validateSignupRequestFormat(signUpRequest);
        signUpValidator.checkSpecialLetter(signUpRequest.memberPassword());
        signUpValidator.checkPasswordLength(signUpRequest.memberPassword());

        emailValidator.validateEmailFormat(signUpRequest.memberEmail());
        authValidator.checkDuplicateMemberNickName(signUpRequest.memberNickName());
        authValidator.checkDuplicateMemberEmail(signUpRequest.memberEmail());


        String encodedPassword = passwordEncoder.encode(signUpRequest.memberPassword());  // 암호화
        String imageUrl = s3Uploader.uploadFile(corporationImage);

        corporationValidator.checkBusinessNumber(signUpRequest, corporationImage);
        Member member = AuthMapper.toMember(signUpRequest, encodedPassword, imageUrl);  // 암호화된 비밀번호 전달

        return memberJpaRepository.save(member);
    }

    public Member normalSignUp(NormalSignUpRequest signUpRequest) {
        signUpValidator.normalValidateSignupRequestFormat(signUpRequest);
        emailValidator.validateEmailFormat(signUpRequest.memberEmail());
        signUpValidator.checkPasswordLength(signUpRequest.memberPassword());
        signUpValidator.checkSpecialLetter(signUpRequest.memberPassword());

        authValidator.checkDuplicateMemberNickName(signUpRequest.memberNickName());
        authValidator.checkDuplicateMemberEmail(signUpRequest.memberEmail());

        String encodedPassword = passwordEncoder.encode(signUpRequest.memberPassword());  // 암호화

        Member member = AuthMapper.toNormalMember(signUpRequest, encodedPassword);  // 암호화된 비밀번호 전달
        return memberJpaRepository.save(member);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        signUpValidator.validateLoginRequestFormat(loginRequest);
        Member member = authValidator.findMemberByEmail(loginRequest.memberEmail());

        AuthValidator.validatePasswordEncoderException(passwordEncoder.matches(loginRequest.memberPassword(), member.getMemberPassword()));

        String token = jwtTokenProvider.createToken(member.getId());
        return AuthMapper.toLoginResponse(token, member);
    }

    @Transactional
    public void changePassword(String email, String newPassword, String newPasswordConfirm) {
        Member member = authValidator.findMemberByEmail(email);
        validateNewPassword(newPassword, newPasswordConfirm);
        signUpValidator.checkPasswordLength(newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);  // 암호화
        member.updatePassword(encodedPassword);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !memberJpaRepository.existsByMemberEmail(email);
    }

    private static void validatePasswordEncoderException(final boolean passwordEncoder) {
        if (!passwordEncoder) {
            throw new NotSamePasswordException();
        }
    }

    private static void validateNewPassword(final String newPassword, final String newPasswordConfirm) {
        validatePasswordEncoderException(newPassword.equals(newPasswordConfirm));
    }

}
