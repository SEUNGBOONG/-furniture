package com.example.demo.login.member.domain.auth;

import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.NormalSignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.exception.exceptions.auth.InvalidLoginRequestException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidPasswordFormatException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidSignUpRequestException;

import org.springframework.stereotype.Component;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class SignUpValidator {

    public void validateSignupRequestFormat(SignUpRequest signUpRequest) {
        if (signUpRequest == null ||
                isEmpty(signUpRequest.memberEmail()) ||
                isEmpty(signUpRequest.memberName()) ||
                isEmpty(signUpRequest.memberPassword()) ||
                isEmpty(signUpRequest.memberNickName())) {
            throw new InvalidSignUpRequestException();
        }
    }

    public void normalValidateSignupRequestFormat(NormalSignUpRequest signUpRequest) {
        if (signUpRequest == null ||
                isEmpty(signUpRequest.memberEmail()) ||
                isEmpty(signUpRequest.memberName()) ||
                isEmpty(signUpRequest.memberPassword()) ||
                isEmpty(signUpRequest.memberNickName())) {
            throw new InvalidSignUpRequestException();
        }
    }

    public void checkPasswordLength(String password) {
        if (password.length() <= 7) {
            throw new InvalidPasswordFormatException();
        }
    }

    public void validateLoginRequestFormat(LoginRequest loginRequest) {
        if (loginRequest == null ||
                isEmpty(loginRequest.memberEmail()) ||
                isEmpty(loginRequest.memberPassword())) {
            throw new InvalidLoginRequestException();
        }
    }
}
