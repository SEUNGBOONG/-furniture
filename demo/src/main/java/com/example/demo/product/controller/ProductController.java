package com.example.demo.product.controller;

import com.example.demo.common.Setting;
import com.example.demo.login.global.annotation.Member;

import com.example.demo.product.controller.dto.CategoryResponse;
import com.example.demo.product.controller.dto.ProductRequest;
import com.example.demo.product.controller.dto.ProductResponse;
import com.example.demo.product.service.ProductService;

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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest dto, @Member Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        productService.createProduct(dto);
        return ResponseEntity.ok(Setting.PRODUCT_CREATE_SUCCESS.toString());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                           @RequestBody ProductRequest dto,
                                           @Member Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }

        productService.updateProduct(productId, dto);
        return ResponseEntity.ok(Setting.PRODUCT_UPDATE_SUCCESS.toString());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                           @Member Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }

        productService.deleteProduct(productId);
        return ResponseEntity.ok(Setting.PRODUCT_DELETE_SUCCESS.toString());
    }

}
