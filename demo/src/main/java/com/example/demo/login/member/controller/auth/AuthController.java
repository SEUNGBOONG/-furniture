package com.example.demo.login.member.controller.auth;

import com.example.demo.common.Setting;
import com.example.demo.login.email.util.EmailSenderUtil;
import com.example.demo.login.member.controller.auth.dto.ChangePasswordRequest;
import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;
import com.example.demo.login.member.controller.dto.AuthRequestDTO;
import com.example.demo.login.member.controller.dto.EmailAuthRequestDto;
import com.example.demo.login.member.mapper.auth.AuthMapper;
import com.example.demo.login.member.service.auth.AuthService;
import com.example.demo.login.util.CorporationValidator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final EmailSenderUtil emailSenderUtil;
    private final CorporationValidator corporationValidator;

    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signUp(
            @RequestPart("signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value = "corporationImage", required = false) MultipartFile corporationImage,
            HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(Setting.AUTHENTICATED.toString() + signUpRequest.memberEmail());

        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }

        ResponseEntity<String> body = getStringResponseEntity(signUpRequest, session);
        if (body != null) return body;

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest, corporationImage));
        session.removeAttribute(Setting.AUTHENTICATED.toString() + signUpRequest.memberEmail());
        session.removeAttribute(Setting.CORPORATION_AUTHENTICATED.toString() + signUpRequest.corporationNumber());

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }


    @PostMapping("/validate-corporation")
    public ResponseEntity<String> validateCorporationNumber(@RequestBody BusinessNumberRequest request, HttpSession session) {
        String businessNumber = request.getBusinessNumber();
        return getStringResponseEntity(session, businessNumber);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        log.info("로그인 성공");
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/send-auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailAuthRequestDto emailDto, HttpSession session)
            throws MessagingException, UnsupportedEncodingException {
        String code = emailSenderUtil.sendEmail(emailDto.getEmail(), session);

        session.setAttribute(emailDto.getEmail(), code);
        session.setAttribute(Setting.AUTHENTICATED.toString() + emailDto.getEmail(), false);

        return ResponseEntity.ok(Setting.SUCCEED_SENDER_CERTIFICATION_NUMBER.toString());
    }

    @PostMapping("/verify-auth-code")
    public ResponseEntity<String> verifyPasswordCode(@RequestBody AuthRequestDTO request, HttpSession session) {
        String storedCode = (String) session.getAttribute(request.getEmail());

        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            session.removeAttribute(request.getEmail());
            session.setAttribute(Setting.AUTHENTICATED.toString() + request.getEmail(), true);
            return ResponseEntity.ok(Setting.SUCCEED_CERTIFICATION_NUMBER.toString());
        }

        return ResponseEntity.badRequest().body(Setting.FAIL_CERTIFICATION_NUMBER.toString());
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute(Setting.AUTHENTICATED.toString() + request.getEmail());

        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }

        authService.changePassword(
                request.getEmail(),
                request.getNewPassword(),
                request.getNewPasswordConfirm()
        );

        session.removeAttribute(Setting.AUTHENTICATED.toString() + request.getEmail());

        return ResponseEntity.ok(Setting.PASSWORD_CHANGE_SUCCESS.toString());
    }

    private static ResponseEntity<String> getStringResponseEntity(final SignUpRequest signUpRequest, final HttpSession session) {
        Boolean isCorpAuthenticated = (Boolean) session.getAttribute(Setting.CORPORATION_AUTHENTICATED.toString() + signUpRequest.corporationNumber());
        if (signUpRequest.checkCorporation()) {
            if (isCorpAuthenticated == null || !isCorpAuthenticated) {
                return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST.toString());
            }
        }
        return null;
    }

    private ResponseEntity<String> getStringResponseEntity(final HttpSession session, final String businessNumber) {
        boolean isValid = corporationValidator.isValidCorporationNumber(businessNumber);

        if (isValid) {
            session.setAttribute(Setting.BUSINESS_CHECK.toString() + businessNumber, true);
            return ResponseEntity.ok(Setting.SUCCESS.toString());
        } else {
            return ResponseEntity.badRequest().body(Setting.NOT_FOUND_BUSINESS_NUMBER.toString());
        }
    }

    @Data
    public static class ResetPasswordRequest {
        private String email;
        private String nickName;
    }

    @Getter
    public static class EmailRequest {
        private String email;

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Data
    public static class BusinessNumberRequest {
        private String businessNumber;
    }
}
