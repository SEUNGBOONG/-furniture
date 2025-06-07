package com.example.demo.login.member.controller.auth;

import com.example.demo.common.Setting;
import com.example.demo.login.email.util.EmailSenderUtil;
import com.example.demo.login.member.controller.auth.dto.AuthRequestDTO;
import com.example.demo.login.member.controller.auth.dto.ChangePasswordRequest;
import com.example.demo.login.member.controller.auth.dto.EmailAuthRequestDto;
import com.example.demo.login.member.controller.auth.dto.EmailCheckRequest;
import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.NormalSignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;
import com.example.demo.login.member.mapper.auth.AuthMapper;
import com.example.demo.login.member.service.auth.AuthService;
import com.example.demo.login.util.CorporationValidator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final EmailSenderUtil emailSenderUtil;
    private final CorporationValidator corporationValidator;

    @PostMapping(value = "/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signUp(
            @RequestPart("signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value = "corporationImage") MultipartFile corporationImage,
            HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(getAuthKey(signUpRequest.memberEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = getStringResponseEntity(isAuthenticated);
        if (isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST)) return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        Boolean isCorpAuthenticated = (Boolean) session.getAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));
        if (signUpRequest.checkCorporation() && (isCorpAuthenticated == null || !isCorpAuthenticated)) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_THE_BUSINESS_CERTIFICATION_FIRST.toString());
        }

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.signUp(signUpRequest, corporationImage));
        session.removeAttribute(getAuthKey(signUpRequest.memberEmail()));
        session.removeAttribute(getCorpAuthKey(signUpRequest.corporationNumber()));

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/normalMembers")
    public ResponseEntity<?> normalSignUp(@RequestBody NormalSignUpRequest request, HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(getAuthKey(request.memberEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = getStringResponseEntity(isAuthenticated);
        if (isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST)) return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.normalSignUp(request));
        session.removeAttribute(getAuthKey(request.memberEmail()));

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    private static boolean isaBoolean(final ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST) {
        return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST != null;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/send-auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailAuthRequestDto emailDto, HttpSession session)
            throws MessagingException, UnsupportedEncodingException {
        String code = emailSenderUtil.sendEmail(emailDto.getEmail(), session);
        session.setAttribute(emailDto.getEmail(), code);
        session.setAttribute(getAuthKey(emailDto.getEmail()), false);
        return ResponseEntity.ok(Setting.SUCCEED_SENDER_CERTIFICATION_NUMBER.toString());
    }

    @PostMapping("/verify-auth-code")
    public ResponseEntity<String> verifyPasswordCode(@RequestBody AuthRequestDTO request, HttpSession session) {
        String storedCode = (String) session.getAttribute(request.getEmail());
        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            session.removeAttribute(request.getEmail());
            session.setAttribute(getAuthKey(request.getEmail()), true);

            return ResponseEntity.ok(Setting.SUCCEED_CERTIFICATION_NUMBER.toString());
        }
        return ResponseEntity.badRequest().body(Setting.FAIL_CERTIFICATION_NUMBER.toString());
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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute(getAuthKey(request.getEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = getStringResponseEntity(isAuthenticated);
        if (isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST)) return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        authService.changePassword(request.getEmail(), request.getNewPassword(), request.getNewPasswordConfirm());
        session.removeAttribute(getAuthKey(request.getEmail()));
        return ResponseEntity.ok(Setting.PASSWORD_CHANGE_SUCCESS.toString());
    }

    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestBody EmailCheckRequest request) {
        boolean isAvailable = authService.isEmailAvailable(request.getEmail());
        if (isAvailable) {
            return ResponseEntity.ok(Setting.EMAIL_AVAILABLE.toString());
        } else {
            return ResponseEntity.status(409).body(Setting.EMAIL_ALREADY_EXISTS.toString());
        }
    }

    private String getAuthKey(String email) {
        return Setting.AUTHENTICATED.toString() + ":" + email;
    }

    private String getCorpAuthKey(String number) {
        return Setting.CORPORATION_AUTHENTICATED.toString() + ":" + number;
    }

    private static ResponseEntity<String> getStringResponseEntity(final Boolean isAuthenticated) {
        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }
        return null;
    }

    @Data
    public static class BusinessNumberRequest {
        private String businessNumber;
    }
}
