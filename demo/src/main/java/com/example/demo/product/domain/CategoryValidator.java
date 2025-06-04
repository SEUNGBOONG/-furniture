package com.example.demo.product.domain;

import com.example.demo.common.Setting;
import com.example.demo.product.controller.dto.CategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {

    public void validateEmptyCategory(final CategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(Setting.CATEGORY_NAME_REQUIRED.toString());
        }
    }
}
