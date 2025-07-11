package com.example.demo.login.member.domain.auth;

import com.example.demo.common.exception.Setting;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public static ResponseEntity<String> getStringResponseEntity(final Boolean isAuthenticated) {
        if (isAuthenticated == null || !isAuthenticated) {
            return ResponseEntity.status(403).body(Setting.PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST.toString());
        }
        return null;
    }

    public static String getAuthKey(String email) {
        return Setting.AUTHENTICATED.toString() + ":" + email;
    }

    public static boolean isaBoolean(final ResponseEntity<String> PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST) {
        return PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST != null;
    }
}
