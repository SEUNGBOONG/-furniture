package com.example.demo.product.controller.product;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.global.annotation.Member;

import com.example.demo.product.controller.category.dto.CategoryResponse;
import com.example.demo.product.controller.product.dto.ProductDetailSimpleDTO;
import com.example.demo.product.controller.product.dto.ProductRequest;
import com.example.demo.product.controller.product.dto.ProductResponse;
import com.example.demo.common.util.AdminValidator;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.service.product.ProductDetailService;
import com.example.demo.product.service.product.ProductLikeService;
import com.example.demo.product.service.product.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductLikeService productLikeService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createProduct(
            @RequestPart("product") ProductRequest dto,
            @RequestPart("image") MultipartFile image,
            @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        CompletableFuture<String> imageUrlFuture = productService.uploadImageAsync(image);

        String imageUrl = imageUrlFuture.join();
        productService.createProduct(dto, imageUrl);

        return ResponseEntity.ok(Setting.PRODUCT_CREATE_SUCCESS.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductResponse response = getProductResponse(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> getProductsByTagName(@RequestParam String tagName) {
        return ResponseEntity.ok(productService.getProductsByTagName(tagName));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/tags")
    public ResponseEntity<Map<String, Object>> getCategoryNameAndTags(@RequestParam Long categoryId) {
        Map<String, Object> response = productService.getCategoryNameAndTags(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/product-details/by-product/{productId}")
    public ResponseEntity<List<ProductDetailSimpleDTO>> getProductDetailsByProductId(@PathVariable Long productId) {
        List<ProductDetailSimpleDTO> dtos = productDetailService.getProductDetailsByProductId(productId);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestPart("product") ProductRequest dto,
                                                @RequestPart("image") MultipartFile image,
                                                @Member Long memberId) {
        // 관리자 권한 체크
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        // 상품 업데이트
        productService.updateProduct(productId, dto, image);
        return ResponseEntity.ok(Setting.PRODUCT_UPDATE_SUCCESS.toString());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                                @Member Long memberId) {
        // 관리자 권한 체크
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        // 상품 삭제
        productService.deleteProduct(productId);
        return ResponseEntity.ok(Setting.PRODUCT_DELETE_SUCCESS.toString());
    }

    @PostMapping("/product-details/{detailId}/like-toggle")
    public ResponseEntity<?> toggleLikeDetail(@PathVariable Long detailId,
                                              @Member Long memberId) {
        boolean isLiked = productLikeService.toggleLike(memberId, detailId);
        return ResponseEntity.ok(Map.of(
                "liked", isLiked,
                "message", isLiked ? "찜 추가됨" : "찜 해제됨"
        ));
    }

    @GetMapping("/likes/details")
    public ResponseEntity<?> getLikedDetails(@Member Long memberId) {
        List<ProductDetail> likedDetails = productLikeService.getLikedProductDetails(memberId);

        return ResponseEntity.ok(
                likedDetails.stream()
                        .map(d -> Map.of(
                                "id", d.getId(),
                                "model", d.getModel(),
                                "size", d.getSize(),
                                "price", d.getPrice(),
                                "productName", d.getProduct().getName(),
                                "image", d.getProduct().getImage()   // ✅ image만 포함
                        ))
                        .toList()
        );
    }

    private static ProductResponse getProductResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getTagName(),
                product.getImage(),
                product.getImage2()
        );
    }
}
