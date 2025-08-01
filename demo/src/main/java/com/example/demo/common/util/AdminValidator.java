package com.example.demo.common.util;

import com.example.demo.common.exception.Setting;
import com.example.demo.product.controller.category.dto.CategoryRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminValidator {

    public static ResponseEntity<String> getStringResponseEntity(final Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        return null;
    }

    public static void validateEmptyCategory(final CategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(Setting.CATEGORY_NAME_REQUIRED.toString());
        }
    }
}
