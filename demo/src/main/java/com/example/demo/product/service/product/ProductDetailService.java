package com.example.demo.product.service.product;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.util.MemberValidator;
import com.example.demo.product.controller.product.dto.ProductDetailSimpleDTO;
import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.domain.repository.product.ProductDetailRepository;
import com.example.demo.product.domain.repository.product.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductLikeRepository productLikeRepository;
    private final MemberValidator memberValidator;

    // 로그인 안 했을 때 (isLiked = false)
    public List<ProductDetailSimpleDTO> getProductDetailsByProductId(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.findByProduct_Id(productId);
        return productDetails.stream()
                .map(pd -> new ProductDetailSimpleDTO(
                        pd.getId(),
                        pd.getModel(),
                        pd.getSize(),
                        pd.getPrice(),
                        false
                ))
                .toList();
    }

    // 로그인 했을 때 (isLiked 체크)
    public List<ProductDetailSimpleDTO> getProductDetailsByProductId(Long productId, Long memberId) {
        Member member = memberValidator.getMember(memberId);
        List<ProductDetail> productDetails = productDetailRepository.findByProduct_Id(productId);

        return productDetails.stream()
                .map(pd -> new ProductDetailSimpleDTO(
                        pd.getId(),
                        pd.getModel(),
                        pd.getSize(),
                        pd.getPrice(),
                        productLikeRepository.findByMemberAndProductDetail(member, pd).isPresent()
                ))
                .toList();
    }
}
