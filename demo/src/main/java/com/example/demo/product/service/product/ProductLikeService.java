package com.example.demo.product.service.product;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.util.MemberValidator;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.domain.entity.product.ProductLike;
import com.example.demo.product.domain.repository.product.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductService productService;
    private final MemberValidator memberValidator;

    // ✅ 상품 단위로 찜 토글 (로그인 필요)
    public boolean toggleLike(Long memberId, Long productId) {
        Member member = memberValidator.getMember(memberId); // 토글은 로그인 필수
        Product product = productService.findById(productId);

        Optional<ProductLike> like = productLikeRepository.findByMemberAndProduct(member, product);

        if (like.isPresent()) {
            productLikeRepository.delete(like.get());
            return false; // 해제됨
        } else {
            productLikeRepository.save(ProductLike.builder()
                    .member(member)
                    .product(product)
                    .build());
            return true; // 추가됨
        }
    }

    // ✅ 상품 좋아요 여부 체크 (로그인 안 한 경우 → false)
    public boolean isProductLiked(Long memberId, Long productId) {
        if (memberId == null) return false; // 로그인 안 했으면 무조건 false

        Member member = memberValidator.getMember(memberId);
        Product product = productService.findById(productId);

        return productLikeRepository.findByMemberAndProduct(member, product).isPresent();
    }

    // ✅ 내가 찜한 상품 조회 (로그인 필요)
    public List<Product> getLikedProducts(Long memberId) {
        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        List<ProductLike> likes = productLikeRepository.findAllByMemberId(memberId);
        return likes.stream()
                .map(ProductLike::getProduct)
                .toList();
    }
}
