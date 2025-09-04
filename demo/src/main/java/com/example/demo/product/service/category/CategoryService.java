package com.example.demo.product.service.category;

import com.example.demo.common.exception.Setting;

import com.example.demo.product.controller.category.dto.CategoryRequest;
import com.example.demo.product.controller.category.dto.CategoryResponse;
import com.example.demo.common.util.AdminValidator;
import com.example.demo.product.domain.entity.category.Category;
import com.example.demo.product.domain.repository.category.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryRequest request) {

        AdminValidator.validateEmptyCategory(request);

        Category category = new Category(request.getName());
        categoryRepository.save(category);
    }

    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return CategoryResponse.from(category);
    }

    public void updateCategory(Long categoryId, CategoryRequest request) {
        Category category = validateNotFoundCategory(categoryId);
        category.updateName(request.getName());
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    private Category validateNotFoundCategory(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.NOT_FOUND_CATEGORY.toString()));
    }
}
