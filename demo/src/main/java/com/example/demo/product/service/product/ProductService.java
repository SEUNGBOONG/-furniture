package com.example.demo.product.service.product;

import com.example.demo.common.exception.Setting;
import com.example.demo.config.s3.S3Uploader;

import com.example.demo.product.controller.category.dto.CategoryResponse;
import com.example.demo.product.controller.product.dto.ProductRequest;
import com.example.demo.product.controller.product.dto.ProductResponse;

import com.example.demo.product.domain.entity.category.Category;
import com.example.demo.product.domain.repository.category.CategoryRepository;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;

    // 상품 생성
    public void createProduct(ProductRequest request, String imageUrl) {
        Category category = getCategory(request);
        Product product = getProduct(Product.builder(), request, category, imageUrl);

        productRepository.save(product);
    }

    // 상품 업데이트
    public void updateProduct(Long productId, ProductRequest request, MultipartFile imageFile) {
        Product product = getProduct(productId);
        Category category = getCategory(request);
        Result result = new Result(product, category);

        String imageUrl = s3Uploader.uploadFile(imageFile); // 이미지 다시 업로드

        Product updated = getProduct(Product.builder()
                .id(result.product().getId()), request, result.category(), imageUrl);

        productRepository.save(updated);
    }

    public Product findById(Long productId) {
        return getProduct(productId);
    }

    @Async
    public CompletableFuture<String> uploadImageAsync(MultipartFile imageFile) {
        String imageUrl = s3Uploader.uploadFile(imageFile);
        return CompletableFuture.completedFuture(imageUrl);
    }

    // 기타 서비스 메서드
    public List<ProductResponse> getProductsByTagName(String tagName) {
        return productRepository.findByTagName(tagName).stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategory().getName(), p.getTagName(), p.getImage()))
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategory().getName(), p.getTagName(), p.getImage()))
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long productId) {
        Product product = getProduct(productId);
        productRepository.delete(product);
    }

    public Map<String, Object> getCategoryNameAndTags(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(Setting.CATEGORY_NOT_FOUND)));

        List<String> tagNames = productRepository.findDistinctTagNamesByCategoryId(categoryId);

        return Map.of(
                "categoryName", category.getName(),
                "tagNames", tagNames
        );
    }

    // 카테고리 조회
    private Category getCategory(final ProductRequest request) {
        return categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(Setting.CATEGORY_NOT_FOUND.toString()));
    }

    // 상품 조회
    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.PRODUCT_NOT_FOUND.toString()));
    }

    // 상품 빌더
    private static Product getProduct(final Product.ProductBuilder builder, final ProductRequest request, final Category category, final String imageUrl) {
        return builder
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .tagName(request.getTag())
                .image(imageUrl)
                .build();
    }

    // 결과 클래스 (상품, 카테고리)
    private record Result(Product product, Category category) {
    }
}
