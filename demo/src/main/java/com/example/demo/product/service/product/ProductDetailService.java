package com.example.demo.product.service.product;

import com.example.demo.product.controller.product.dto.ProductDetailSimpleDTO;
import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.domain.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    // ✅ 상품에 속한 옵션(사이즈/모델) 조회
    public List<ProductDetailSimpleDTO> getProductDetailsByProductId(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.findByProduct_Id(productId);
        return productDetails.stream()
                .map(pd -> new ProductDetailSimpleDTO(
                        pd.getId(),
                        pd.getModel(),
                        pd.getSize(),
                        pd.getPrice()
                ))
                .toList();
    }
}
