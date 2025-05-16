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
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    public static final String AUTHENTICATED = "AUTHENTICATED_";

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/members")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute(AUTHENTICATED + signUpRequest.memberEmail());

        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest));
        URI location = URI.create("/members/" + response.id());
        log.info("회원가입 완료 - ID: {}, 닉네임: {}", response.id(), response.memberNickname());

        // 인증 정보 제거
        session.removeAttribute(AUTHENTICATED + signUpRequest.memberEmail());

        return ResponseEntity.created(location).body(response);
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

        // 세션에서 가져온 인증 코드 로그로 출력하여 확인
        // 입력한 코드와 세션에 저장된 코드 비교
        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            session.removeAttribute(request.getEmail());  // 인증 성공 후 세션에서 인증 코드 삭제
            session.setAttribute(AUTHENTICATED + request.getEmail(), true);  // 인증 완료 상태 저장
            return ResponseEntity.ok(Setting.SUCCEED_CERTIFICATION_NUMBER.toString());
        }

        // 인증 실패 시
        return ResponseEntity.badRequest().body(Setting.FAIL_CERTIFICATION_NUMBER.toString());
    }


    // EmailRequest 클래스
    @Getter
    public static class EmailRequest {
        private String email;
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
