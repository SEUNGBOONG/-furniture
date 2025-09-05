package com.example.demo.product.controller.product;

import com.example.demo.common.exception.Setting;
import com.example.demo.login.global.annotation.Member;
import com.example.demo.product.controller.category.dto.CategoryResponse;
import com.example.demo.product.controller.product.dto.ProductDetailSimpleDTO;
import com.example.demo.product.controller.product.dto.ProductRequest;
import com.example.demo.product.controller.product.dto.ProductResponse;
import com.example.demo.common.util.AdminValidator;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.service.product.ProductDetailService;
import com.example.demo.product.service.product.ProductLikeService;
import com.example.demo.product.service.product.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // ✅ 상품 등록
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

    // ✅ 단일 상품 조회 (좋아요 여부 포함)
// ✅ 단일 상품 조회 (로그인 안해도 가능)
// ✅ 단일 상품 조회 (로그인 안해도 가능, 로그인하면 liked 반영)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id,
                                                          @Member Long memberId) {
        Product product = productService.findById(id);

        boolean isLiked = (memberId != null) && productLikeService.isProductLiked(memberId, id);

        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getTagName(),
                product.getImage(),
                product.getImage2(),
                isLiked
        );
        return ResponseEntity.ok(response);
    }

    // ✅ 태그명으로 상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> getProductsByTagName(@RequestParam String tagName,
                                                                      @Member Long memberId) {
        List<Product> products = productService.getProductsByTagName(tagName);
        return ResponseEntity.ok(
                products.stream()
                        .map(p -> new ProductResponse(
                                p.getId(),
                                p.getName(),
                                p.getDescription(),
                                p.getPrice(),
                                p.getCategory().getName(),
                                p.getTagName(),
                                p.getImage(),
                                p.getImage2(),
                                memberId != null && productLikeService.isProductLiked(memberId, p.getId())
                        ))
                        .toList()
        );
    }

    // ✅ 카테고리 전체 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    // ✅ 카테고리명 + 태그 조회
    @GetMapping("/tags")
    public ResponseEntity<Map<String, Object>> getCategoryNameAndTags(@RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.getCategoryNameAndTags(categoryId));
    }

    // ✅ 카테고리별 상품 조회
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(
                products.stream()
                        .map(p -> new ProductResponse(
                                p.getId(),
                                p.getName(),
                                p.getDescription(),
                                p.getPrice(),
                                p.getCategory().getName(),
                                p.getTagName(),
                                p.getImage(),
                                p.getImage2(),
                                false // 로그인 정보 없으니 무조건 false
                        ))
                        .toList()
        );
    }

    // ✅ 상품 상세(옵션) 조회
    @GetMapping("/product-details/by-product/{productId}")
    public ResponseEntity<List<ProductDetailSimpleDTO>> getProductDetailsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productDetailService.getProductDetailsByProductId(productId));
    }

    // ✅ 상품 수정
    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestPart("product") ProductRequest dto,
                                                @RequestPart("image") MultipartFile image,
                                                @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        productService.updateProduct(productId, dto, image);
        return ResponseEntity.ok(Setting.PRODUCT_UPDATE_SUCCESS.toString());
    }

    // ✅ 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                                @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        productService.deleteProduct(productId);
        return ResponseEntity.ok(Setting.PRODUCT_DELETE_SUCCESS.toString());
    }

    // ✅ 상품 좋아요 토글
    @PostMapping("/{productId}/like-toggle")
    public ResponseEntity<?> toggleLike(@PathVariable Long productId,
                                        @Member Long memberId) {
        boolean isLiked = productLikeService.toggleLike(memberId, productId);
        return ResponseEntity.ok(Map.of(
                "liked", isLiked,
                "message", isLiked ? "찜 추가됨" : "찜 해제됨"
        ));
    }

    // ✅ 내가 찜한 상품 조회
    @GetMapping("/likes")
    public ResponseEntity<?> getLikedProducts(@Member Long memberId) {
        List<Product> likedProducts = productLikeService.getLikedProducts(memberId);
        return ResponseEntity.ok(
                likedProducts.stream()
                        .map(p -> new ProductResponse(
                                p.getId(),
                                p.getName(),
                                p.getDescription(),
                                p.getPrice(),
                                p.getCategory().getName(),
                                p.getTagName(),
                                p.getImage(),
                                p.getImage2(),
                                true // ✅ 조회되는 건 전부 좋아요한 상품
                        ))
                        .toList()
        );
    }
}
