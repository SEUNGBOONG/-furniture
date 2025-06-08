package com.example.demo.product.service;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.domain.member.MemberValidator;
import com.example.demo.product.domain.entity.Product;
import com.example.demo.product.domain.entity.ProductLike;
import com.example.demo.product.domain.repository.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final MemberValidator memberValidator;

    public boolean toggleLike(Long memberId, Product product) {
        Member member = memberValidator.getMember(memberId);

        return toggleFunction(product, member);
    }

    public List<Product> getLikedProducts(Long memberId) {
        List<ProductLike> likes = productLikeRepository.findAllByMemberId(memberId);
        return likes.stream()
                .map(ProductLike::getProduct)
                .toList();
    }

    private boolean toggleFunction(final Product product, final Member member) {
        Optional<ProductLike> like = productLikeRepository.findByMemberAndProduct(member, product);

        if (like.isPresent()) {
            productLikeRepository.delete(like.get());
            return false;
        } else {
            productLikeRepository.save(ProductLike
                    .builder()
                    .member(member)
                    .product(product)
                    .build());
            return true;
        }
    }
}
