package com.example.demo.product.service;

import com.example.demo.common.Setting;

import com.example.demo.product.controller.dto.CategoryRequest;
import com.example.demo.product.controller.dto.CategoryResponse;
import com.example.demo.product.domain.CategoryValidator;
import com.example.demo.product.domain.entity.Category;
import com.example.demo.product.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public void createCategory(CategoryRequest request) {
        // 카테고리 이름이 비어 있는지 확인 하는 로직
        categoryValidator.validateEmptyCategory(request);

        Category category = new Category(request.getName());
        categoryRepository.save(category);
    }

    public void updateCategory(Long categoryId, CategoryRequest request) {
        Category category = validateFindByIdException(categoryId);
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

    private Category validateFindByIdException(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(Setting.NOT_FOUND_CATEGORY.toString()));
    }
}
