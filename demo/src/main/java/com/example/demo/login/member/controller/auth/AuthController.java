package com.example.demo.login.member.controller.auth;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.email.util.EmailSenderUtil;
import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;

import com.example.demo.login.member.domain.auth.AuthUtil;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.mapper.auth.AuthMapper;
import com.example.demo.login.member.service.auth.AuthService;

import com.example.demo.login.util.CorporationValidator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
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

import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final CorporationValidator corporationValidator;
    private final EmailSenderUtil emailSenderUtil;

    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signUp(
            @RequestPart("signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value = "corporationImage") MultipartFile corporationImage,
            HttpSession session) {

        if (!emailSenderUtil.isEmailVerified(signUpRequest.memberEmail())) {
            return ResponseEntity.status(403)
                    .body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }

        Boolean isCorpAuthenticated = (Boolean) session.getAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));
        if (signUpRequest.checkCorporation() && (isCorpAuthenticated == null || !isCorpAuthenticated)) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST.toString());
        }

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest, corporationImage));

        // ✅ 이메일 인증 플래그 정리
        emailSenderUtil.clearVerifiedFlag(signUpRequest.memberEmail());
        session.removeAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = authService.loginAndReturnMember(loginRequest); // 1. 멤버 반환
        String token = authService.generateToken(member.getId());      // 2. 토큰 생성

        // 3. 쿠키 세팅
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);
        jwtCookie.setSecure(true);
        jwtCookie.setDomain("daemyungdesk.com");

        response.addCookie(jwtCookie);

        // 4. 응답 (token 없음)
        LoginResponse loginResponse = AuthMapper.toLoginResponse(member);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie deleteCookie = new Cookie("token", null);
        deleteCookie.setHttpOnly(true);
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0); // 만료
        // 운영 환경이라면 아래도 함께 설정
        deleteCookie.setSecure(true);
        deleteCookie.setDomain("daemyungdesk.com");

        response.addCookie(deleteCookie);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/validate-corporation")
    public ResponseEntity<String> validateCorporationNumber(@RequestBody BusinessNumberRequest request, HttpSession session) {
        boolean isValid = corporationValidator.isValidCorporationNumber(request.getBusinessNumber().trim());
        if (isValid) {
            session.setAttribute(getCorpAuthKey(request.getBusinessNumber()), true);
            return ResponseEntity.ok(Setting.SUCCESS.toString());
        } else {
            return ResponseEntity.badRequest().body(Setting.NOT_FOUND_BUSINESS_NUMBER.toString());
        }
    }

    private String getCorpAuthKey(String number) {
        return Setting.CORPORATION_AUTHENTICATED.toString() + ":" + number;
    }

    @Data
    public static class BusinessNumberRequest {
        private String businessNumber;
    }
}
