package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.product.domain.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByMemberId(Long memberId);
    List<CartItem> findByMemberAndProductIdIn(Member member, List<Long> productIds);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart_item (product_id, member_id, quantity) " +
            "VALUES (:productId, :memberId, :quantity) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + :quantity", nativeQuery = true)
    void upsertCartItem(Long productId, Long memberId, int quantity);

    @Query("SELECT DISTINCT c FROM CartItem c JOIN FETCH c.product WHERE c.member.id = :memberId")
    List<CartItem> findDistinctByMemberId(Long memberId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_item SET quantity = quantity + :amount WHERE id = :cartItemId AND member_id = :memberId", nativeQuery = true)
    int updateQuantityByAmount(@Param("cartItemId") Long cartItemId,
                               @Param("memberId") Long memberId,
                               @Param("amount") int amount);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM cart_item WHERE member_id = :memberId AND product_id = :productId)", nativeQuery = true)
    Long existsCartItem(@Param("memberId") Long memberId, @Param("productId") Long productId);

}
