package com.example.demo.product.domain.repository.product;
import com.example.demo.product.domain.entity.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByProduct_Id(Long productId);
}
