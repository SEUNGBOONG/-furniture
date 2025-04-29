package com.example.demo.product.service;

import com.example.demo.common.Setting;
import com.example.demo.product.controller.dto.CategoryResponse;
import com.example.demo.product.controller.dto.ProductRequest;
import com.example.demo.product.controller.dto.ProductResponse;
import com.example.demo.product.domain.entity.Category;
import com.example.demo.product.domain.repository.CategoryRepository;
import com.example.demo.product.domain.entity.Product;
import com.example.demo.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(Setting.CATEGORY_NOT_FOUND.toString()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .build();

        productRepository.save(product);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategory().getName()))
                .collect(Collectors.toList());
    }

    public ProductResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.PRODUCT_NOT_FOUND.toString()));
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getName());
    }

    public void updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.PRODUCT_NOT_FOUND.toString()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(Setting.CATEGORY_NOT_FOUND.toString()));

        product = Product.builder()
                .id(product.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .build();

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.PRODUCT_NOT_FOUND.toString()));
        productRepository.delete(product);
    }

}
