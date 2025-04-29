package com.example.demo.login.member.service.auth;


import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.exception.exceptions.auth.DuplicateEmailException;
import com.example.demo.login.member.exception.exceptions.auth.DuplicateNickNameException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidEmailFormatException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidLoginRequestException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidPasswordFormatException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidSignUpRequestException;
import com.example.demo.login.member.exception.exceptions.auth.NotFoundMemberByEmailException;
import com.example.demo.login.member.infrastructure.auth.JwtTokenProvider;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;


import com.example.demo.login.member.mapper.auth.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Transactional
    public Member signUp(SignUpRequest signUpRequest) {
        validateSignupRequestFormat(signUpRequest);
        validateEmailFormat(signUpRequest.memberEmail());
        checkPasswordLength(signUpRequest.memberPassword());
        Member member = AuthMapper.toMember(signUpRequest);
        checkDuplicateMemberNickName(member.getMemberNickName());
        checkDuplicateMemberEmail(member.getMemberEmail());

        return memberJpaRepository.save(member);
    }

    private void validateSignupRequestFormat(SignUpRequest signUpRequest) {
        if (signUpRequest == null ||
                isEmpty(signUpRequest.memberEmail()) ||
                isEmpty(signUpRequest.memberName()) ||
                isEmpty(signUpRequest.memberPassword()) ||
                isEmpty(signUpRequest.memberNickName())) {
            throw new InvalidSignUpRequestException();
        }
    }

    private void validateEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailFormatException();
        }
    }

    private void checkDuplicateMemberNickName(String nickName) {
        if (memberJpaRepository.existsByMemberNickName(nickName)) {
            throw new DuplicateNickNameException();
        }
    }

    private void checkDuplicateMemberEmail(String email) {
        if (memberJpaRepository.existsByMemberEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void checkPasswordLength(String password) {
        if (password.length() <= 7) {
            throw new InvalidPasswordFormatException();
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        validateLoginRequestFormat(loginRequest);
        Member member = findMemberByEmail(loginRequest.memberEmail());
        member.checkPassword(loginRequest.memberPassword());

        String token = jwtTokenProvider.createToken(member.getId());
        return AuthMapper.toLoginResponse(token, member);
    }


    private void validateLoginRequestFormat(LoginRequest loginRequest) {
        if (loginRequest == null ||
                isEmpty(loginRequest.memberEmail()) ||
                isEmpty(loginRequest.memberPassword())) {
            throw new InvalidLoginRequestException();
        }
    }

    private Member findMemberByEmail(String email) {
        return memberJpaRepository.findMemberByMemberEmail(email)
                .orElseThrow(NotFoundMemberByEmailException::new);
    }
}
