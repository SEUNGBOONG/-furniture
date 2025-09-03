package com.example.demo.product.service.product;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.util.MemberValidator;
import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.domain.entity.product.ProductLike;
import com.example.demo.product.domain.repository.product.ProductLikeRepository;
import com.example.demo.product.domain.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductDetailRepository productDetailRepository;
    private final MemberValidator memberValidator;

    // 찜 토글 (사이즈 단위)
    public boolean toggleLike(Long memberId, Long productDetailId) {
        Member member = memberValidator.getMember(memberId);
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 상세가 존재하지 않습니다."));

        Optional<ProductLike> like = productLikeRepository.findByMemberAndProductDetail(member, productDetail);

        if (like.isPresent()) {
            productLikeRepository.delete(like.get());
            return false; // 해제됨
        } else {
            productLikeRepository.save(ProductLike.builder()
                    .member(member)
                    .productDetail(productDetail)
                    .build());
            return true; // 추가됨
        }
    }

    // 내가 찜한 상품 상세 조회
    public List<ProductDetail> getLikedProductDetails(Long memberId) {
        List<ProductLike> likes = productLikeRepository.findAllByMemberId(memberId);
        return likes.stream()
                .map(ProductLike::getProductDetail)
                .toList();
    }
}
