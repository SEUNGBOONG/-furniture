package com.example.demo.product.domain.repository;


import com.example.demo.login.member.domain.member.Member;
import com.example.demo.product.domain.entity.Product;
import com.example.demo.product.domain.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByMemberAndProduct(Member member, Product product);
    List<ProductLike> findAllByMemberId(Long memberId);
}

