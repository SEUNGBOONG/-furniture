package com.example.demo.product.controller;

import com.example.demo.common.Setting;

import com.example.demo.login.global.annotation.Member;

import com.example.demo.product.controller.dto.CategoryRequest;
import com.example.demo.product.controller.dto.CategoryResponse;

import com.example.demo.product.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequest request,
                                            @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = managementResponse(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;
        categoryService.createCategory(request);
        return ResponseEntity.ok(Setting.CATEGORY_CREATE_SUCCESS.toString());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,
                                            @RequestBody CategoryRequest request,
                                            @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = managementResponse(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(Setting.CATEGORY_UPDATE_SUCCESS.toString());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId,
                                            @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = managementResponse(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(Setting.CATEGORY_DELETE_SUCCESS.toString());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    private static ResponseEntity<String> managementResponse(final Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        return null;
    }
}
