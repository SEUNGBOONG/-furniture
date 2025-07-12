package com.example.demo.login.member.controller.auth;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.member.controller.auth.dto.NormalSignUpRequest;
import com.example.demo.login.member.controller.auth.dto.SignUpResponse;
import com.example.demo.login.member.domain.auth.AuthUtil;
import com.example.demo.login.member.mapper.auth.AuthMapper;
import com.example.demo.login.member.service.auth.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class NormalAuthController {

    private final AuthService authService;

    @PostMapping("/normalMembers")
    public ResponseEntity<?> normalSignUp(@RequestBody NormalSignUpRequest request, HttpSession session) {

        Boolean isAuthenticated = (Boolean) session.getAttribute(getAuthKey(request.memberEmail()));
        ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST = AuthUtil.getStringResponseEntity(isAuthenticated);
        if (AuthUtil.isaBoolean(PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST)) return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST;

        SignUpResponse response = AuthMapper.toSignUpResponse(authService.normalSignUp(request));
        session.removeAttribute(getAuthKey(request.memberEmail()));

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    private String getAuthKey(String email) {
        return Setting.AUTHENTICATED.toString() + ":" + email;
    }

}
