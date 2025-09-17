package com.example.demo.product.service.product;

import com.example.demo.common.exception.Setting;
import com.example.demo.config.s3.S3Uploader;
import com.example.demo.product.controller.category.dto.CategoryResponse;
import com.example.demo.product.controller.product.dto.ProductRequest;
import com.example.demo.product.domain.entity.category.Category;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.domain.entity.product.ProductImage;
import com.example.demo.product.domain.repository.category.CategoryRepository;
import com.example.demo.product.domain.repository.product.ProductRepository;
import com.example.demo.product.domain.repository.product.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final S3Uploader s3Uploader;

    // ✅ 상품 생성
    public void createProduct(ProductRequest request, String imageUrl, List<String> extraImageUrls) {
        Category category = getCategory(request);
        Product product = buildProduct(Product.builder(), request, category, imageUrl);

        // 추가 이미지 저장
        if (extraImageUrls != null) {
            for (String url : extraImageUrls) {
                product.addImage(ProductImage.builder().url(url).build());
            }
        }

        productRepository.save(product);
    }

    // ✅ 상품 수정
    public void updateProduct(Long productId, ProductRequest request, MultipartFile imageFile, List<MultipartFile> extraFiles) {
        Product product = getProduct(productId);
        Category category = getCategory(request);

        String imageUrl = s3Uploader.uploadFile(imageFile);

        Product updated = buildProduct(Product.builder()
                .id(product.getId()), request, category, imageUrl);

        // 기존 이미지 비우고 새로 추가
        updated.getImages().clear();
        if (extraFiles != null) {
            for (MultipartFile f : extraFiles) {
                String url = s3Uploader.uploadFile(f);
                updated.addImage(ProductImage.builder().url(url).build());
            }
        }

        productRepository.save(updated);
    }

    // ✅ 단일 상품 조회
    public Product findById(Long productId) {
        return getProduct(productId);
    }

    // ✅ 태그명으로 상품 조회
    public List<Product> getProductsByTagName(String tagName) {
        return productRepository.findByTagName(tagName);
    }

    // ✅ 카테고리별 상품 조회
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // ✅ 전체 카테고리 조회
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .toList();
    }

    // ✅ 카테고리명 + 태그명 리스트 조회
    public Map<String, Object> getCategoryNameAndTags(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(Setting.CATEGORY_NOT_FOUND)));

        List<String> tagNames = productRepository.findDistinctTagNamesByCategoryId(categoryId);

        return Map.of(
                "categoryName", category.getName(),
                "tagNames", tagNames
        );
    }

    // ✅ 상품 삭제
    public void deleteProduct(Long productId) {
        Product product = getProduct(productId);
        productRepository.delete(product);
    }

    // ✅ 이미지 비동기 업로드
    @Async
    public CompletableFuture<String> uploadImageAsync(MultipartFile imageFile) {
        String imageUrl = s3Uploader.uploadFile(imageFile);
        return CompletableFuture.completedFuture(imageUrl);
    }

    // 내부 메서드
    private Category getCategory(final ProductRequest request) {
        return categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(Setting.CATEGORY_NOT_FOUND.toString()));
    }

    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.PRODUCT_NOT_FOUND.toString()));
    }

    private static Product buildProduct(final Product.ProductBuilder builder,
                                        final ProductRequest request,
                                        final Category category,
                                        final String imageUrl) {
        return builder
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .tagName(request.getTag())
                .image(imageUrl)
                .build();
    }
}
