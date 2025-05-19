package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.product.domain.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMemberAndProduct(Member member, Product product);
    List<CartItem> findAllByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
    List<CartItem> findByMemberAndProductIdIn(Member member, List<Long> productIds);

    @Query("SELECT c FROM CartItem c JOIN FETCH c.product JOIN FETCH c.member WHERE c.member = :member AND c.product = :product")
    Optional<CartItem> findByMemberAndProductWithFetch(@Param("member") Member member, @Param("product") Product product);

}
