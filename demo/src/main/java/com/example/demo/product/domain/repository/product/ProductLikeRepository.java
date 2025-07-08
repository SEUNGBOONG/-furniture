package com.example.demo.product.domain.repository.product;


import com.example.demo.login.member.domain.member.Member;
import com.example.demo.product.domain.entity.product.Product;
import com.example.demo.product.domain.entity.product.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByMemberAndProduct(Member member, Product product);
    List<ProductLike> findAllByMemberId(Long memberId);
}

