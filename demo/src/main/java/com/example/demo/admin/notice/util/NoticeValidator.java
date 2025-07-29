package com.example.demo.admin.notice.util;

import com.example.demo.common.exception.Setting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NoticeValidator {

    public static ResponseEntity<String> getAdminValidator(final Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        return null;
    }
}
