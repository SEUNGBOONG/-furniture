package com.example.demo.product.service.product;

import com.example.demo.product.domain.entity.product.ProductDetailImage;
import com.example.demo.product.domain.repository.product.ProductDetailImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailImageService {

    private final ProductDetailImageRepository detailImageRepository;

    public List<String> getDetailImagesByProduct(Long productId) {
        return detailImageRepository.findByProduct_IdOrderBySortOrderAsc(productId)
                .stream()
                .map(ProductDetailImage::getUrl)
                .toList();
    }
}
