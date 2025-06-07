package com.example.demo.product.domain;

import com.example.demo.common.Setting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public static ResponseEntity<String> getStringResponseEntity(final Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        return null;
    }
}
