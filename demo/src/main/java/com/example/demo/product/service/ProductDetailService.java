package com.example.demo.product.service;

import com.example.demo.product.controller.dto.ProductDetailSimpleDTO;
import com.example.demo.product.domain.entity.ProductDetail;
import com.example.demo.product.domain.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    // productId 기준으로 여러 개 조회 후 DTO 리스트 반환
    @Cacheable(value = "productDetails")
    public List<ProductDetailSimpleDTO> getProductDetailsByProductId(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.findByProduct_Id(productId);
        return productDetails.stream()
                .map(pd -> new ProductDetailSimpleDTO(pd.getModel(), pd.getSize()))
                .toList();
    }
}

