package com.example.demo.login.member.controller.auth;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.email.util.EmailSenderUtil;
import com.example.demo.login.member.controller.auth.dto.AuthRequestDTO;
import com.example.demo.login.member.controller.auth.dto.ChangePasswordRequest;
import com.example.demo.login.member.controller.auth.dto.EmailAuthRequestDto;
import com.example.demo.login.member.controller.auth.dto.EmailCheckRequest;
import com.example.demo.login.member.domain.auth.AuthUtil;
import com.example.demo.login.member.service.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthStateController {

    private final EmailSenderUtil emailSenderUtil;
    private final AuthService authService;

    @PostMapping("/send-auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailAuthRequestDto emailDto, HttpSession session)
            throws MessagingException, UnsupportedEncodingException {
        String code = emailSenderUtil.sendEmail(emailDto.getEmail(), session);
        session.setAttribute(emailDto.getEmail(), code);
        session.setAttribute(AuthUtil.getAuthKey(emailDto.getEmail()), false);
        return ResponseEntity.ok(Setting.SUCCEED_SENDER_CERTIFICATION_NUMBER.toString());
    }

    @PostMapping("/verify-auth-code")
    public ResponseEntity<String> verifyPasswordCode(@RequestBody AuthRequestDTO request, HttpSession session) {
        String storedCode = (String) session.getAttribute(request.getEmail());
        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            session.removeAttribute(request.getEmail());
            session.setAttribute(AuthUtil.getAuthKey(request.getEmail()), true);

            return ResponseEntity.ok(Setting.SUCCEED_CERTIFICATION_NUMBER.toString());
        }
        return ResponseEntity.badRequest().body(Setting.FAIL_CERTIFICATION_NUMBER.toString());
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute(AuthUtil.getAuthKey(request.getEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = AuthUtil.getStringResponseEntity(isAuthenticated);
        if (AuthUtil.isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST))
            return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        authService.changePassword(request.getEmail(), request.getNewPassword(), request.getNewPasswordConfirm());
        session.removeAttribute(AuthUtil.getAuthKey(request.getEmail()));
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

}
