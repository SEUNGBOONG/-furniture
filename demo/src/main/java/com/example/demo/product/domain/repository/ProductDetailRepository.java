package com.example.demo.product.domain.repository;
import com.example.demo.product.domain.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByProduct_Id(Long productId);
}


