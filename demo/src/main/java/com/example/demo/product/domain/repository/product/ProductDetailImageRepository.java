package com.example.demo.product.domain.repository.product;

import com.example.demo.product.domain.entity.product.ProductDetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailImageRepository extends JpaRepository<ProductDetailImage, Long> {
    List<ProductDetailImage> findByProduct_IdOrderBySortOrderAsc(Long productId);
}
