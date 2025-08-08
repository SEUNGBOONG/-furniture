package com.example.demo.login.member.controller.auth;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;

import com.example.demo.login.member.domain.auth.AuthUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final CorporationValidator corporationValidator;

    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signUp(
            @RequestPart("signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value = "corporationImage") MultipartFile corporationImage,
            HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(AuthUtil.getAuthKey(signUpRequest.memberEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = AuthUtil.getStringResponseEntity(isAuthenticated);
        if (AuthUtil.isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST))
            return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        Boolean isCorpAuthenticated = (Boolean) session.getAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));
        if (signUpRequest.checkCorporation() && (isCorpAuthenticated == null || !isCorpAuthenticated)) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST.toString());
        }

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest, corporationImage));
        session.removeAttribute(AuthUtil.getAuthKey(signUpRequest.memberEmail()));
        session.removeAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        Cookie jwtCookie = new Cookie("token", loginResponse.token());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);
        jwtCookie.setSecure(true);
//        jwtCookie.setSecure(false);
        jwtCookie.setDomain("daemyungdesk.com");

        response.addCookie(jwtCookie);

        return ResponseEntity.ok().body(loginResponse);
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
