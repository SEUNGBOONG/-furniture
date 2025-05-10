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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createProduct(
            @RequestPart("product") ProductRequest dto,
            @RequestPart("image") MultipartFile image,
            @Member Long memberId) {
        // 관리자 권한 체크
        ResponseEntity<String> FORBIDDEN = getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        // 비동기 이미지 업로드
        CompletableFuture<String> imageUrlFuture = productService.uploadImageAsync(image);

        // 이미지 URL을 비동기적으로 받음
        String imageUrl = imageUrlFuture.join();  // join()으로 비동기 완료 대기

        // 상품 생성
        productService.createProduct(dto, imageUrl);

        return ResponseEntity.ok(Setting.PRODUCT_CREATE_SUCCESS.toString());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> getProductsByTagName(@RequestParam String tagName) {
        return ResponseEntity.ok(productService.getProductsByTagName(tagName));
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

    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestPart("product") ProductRequest dto,
                                                @RequestPart("image") MultipartFile image,
                                                @Member Long memberId) {
        // 관리자 권한 체크
        ResponseEntity<String> FORBIDDEN = getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        // 상품 업데이트
        productService.updateProduct(productId, dto, image);
        return ResponseEntity.ok(Setting.PRODUCT_UPDATE_SUCCESS.toString());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                                @Member Long memberId) {
        // 관리자 권한 체크
        ResponseEntity<String> FORBIDDEN = getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        // 상품 삭제
        productService.deleteProduct(productId);
        return ResponseEntity.ok(Setting.PRODUCT_DELETE_SUCCESS.toString());
    }

    private static ResponseEntity<String> getStringResponseEntity(final Long memberId) {
        if (!memberId.equals(5L)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Setting.FORBIDDEN_ONLY_ADMIN.toString());
        }
        return null;
    }
}
