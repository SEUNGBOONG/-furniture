package com.example.demo.login.member.controller.auth;

import com.example.demo.common.Setting;
import com.example.demo.login.email.service.EmailService;
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

    public static final String AUTHENTICATED = "AUTHENTICATED_";
    public static final String BUSINESS_CHECK = "사업자인증";
    public static final String SUCCESS = "인증에 성공했습니다.";
    public static final String NOT_FOUND_BUSINESS_NUMBER = "없는 사업자 번호입니다.";
    public static final String PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST = "사업자 인증을 먼저 완료해주세요.";
    public static final String CORPORATION_AUTHENTICATED = "CORPORATION_AUTHENTICATED_";

    private final AuthService authService;
    private final EmailService emailService;
    private final CorporationValidator corporationValidator;

    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signUp(
            @RequestPart("signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value = "corporationImage", required = false) MultipartFile corporationImage,
            HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(AUTHENTICATED + signUpRequest.memberEmail());

        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }

        // 사업자 인증 여부 확인 (유효성 검사 외에 실제 인증 여부 확인 필수)
        ResponseEntity<String> body = getStringResponseEntity(signUpRequest, session);
        if (body != null) return body;

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest, corporationImage));
        session.removeAttribute(AUTHENTICATED + signUpRequest.memberEmail());
        session.removeAttribute(CORPORATION_AUTHENTICATED + signUpRequest.corporationNumber());

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
        // 이메일로 인증 코드 전송
        String code = emailService.sendEmail(emailDto.getEmail(), session);

        // 인증 코드 저장 (이메일을 키로 사용)
        session.setAttribute(emailDto.getEmail(), code);
        session.setAttribute(AUTHENTICATED + emailDto.getEmail(), false);

        return ResponseEntity.ok(Setting.SUCCEED_SENDER_CERTIFICATION_NUMBER.toString());
    }


    @PostMapping("/verify-auth-code")
    public ResponseEntity<String> verifyAuthCode(@RequestBody AuthRequestDTO request, HttpSession session) {
        // 이메일 주소로 세션에서 인증 코드 가져오기
        String storedCode = (String) session.getAttribute(request.getEmail());

        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            session.removeAttribute(request.getEmail());  // 인증 성공 후 세션에서 인증 코드 삭제
            session.setAttribute(AUTHENTICATED + request.getEmail(), true);  // 인증 완료 상태 저장
            return ResponseEntity.ok(Setting.SUCCEED_CERTIFICATION_NUMBER.toString());
        }

        // 인증 실패 시
        return ResponseEntity.badRequest().body(Setting.FAIL_CERTIFICATION_NUMBER.toString());
    }

    private ResponseEntity<String> getStringResponseEntity(final HttpSession session, final String businessNumber) {
        boolean isValid = corporationValidator.isValidCorporationNumber(businessNumber);

        if (isValid) {
            session.setAttribute(BUSINESS_CHECK + businessNumber, true);  // 사업자 인증 완료 플래그 세션에 저장
            return ResponseEntity.ok(SUCCESS);
        } else {
            return ResponseEntity.badRequest().body(NOT_FOUND_BUSINESS_NUMBER);
        }
    }
    private static ResponseEntity<String> getStringResponseEntity(final SignUpRequest signUpRequest, final HttpSession session) {
        Boolean isCorpAuthenticated = (Boolean) session.getAttribute(CORPORATION_AUTHENTICATED + signUpRequest.corporationNumber());
        if (signUpRequest.checkCorporation()) {  // 사업자인 경우만 검사
            if (isCorpAuthenticated == null || !isCorpAuthenticated) {
                return ResponseEntity.status(403).body(PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST);
            }
        }
        return null;
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
