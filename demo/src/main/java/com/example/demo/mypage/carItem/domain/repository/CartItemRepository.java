package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMemberAndProduct(Member member, Product product);
    List<CartItem> findAllByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
    List<CartItem> findByMemberAndProductIdIn(Member member, List<Long> productIds);
}
