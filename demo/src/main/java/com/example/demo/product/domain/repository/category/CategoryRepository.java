package com.example.demo.product.domain.repository.category;

import com.example.demo.product.domain.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
