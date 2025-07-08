package com.example.demo.product.domain.repository;

import com.example.demo.product.domain.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByTagName(String tagName);

    @Query("SELECT DISTINCT p.tagName FROM Product p WHERE p.category.id = :categoryId AND p.tagName IS NOT NULL")
    List<String> findDistinctTagNamesByCategoryId(@Param("categoryId") Long categoryId);
}
