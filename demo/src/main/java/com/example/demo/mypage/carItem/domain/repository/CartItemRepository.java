package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByMemberId(Long memberId);
    @Query("""
    SELECT c FROM CartItem c
    JOIN c.productDetail pd
    JOIN pd.product p
    WHERE c.member = :member AND p.id IN :productIds
""")
    List<CartItem> findByMemberAndProductIds(@Param("member") Member member,
                                             @Param("productIds") List<Long> productIds);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart_item (product_detail_id, member_id, quantity, price_at_added) " +
            "VALUES (:productDetailId, :memberId, :quantity, :priceAtAdded) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + :quantity", nativeQuery = true)
    void upsertCartItem(@Param("productDetailId") Long productDetailId,
                        @Param("memberId") Long memberId,
                        @Param("quantity") int quantity,
                        @Param("priceAtAdded") int priceAtAdded);

    @Query("SELECT DISTINCT c FROM CartItem c " +
            "JOIN FETCH c.productDetail pd " +
            "JOIN FETCH pd.product p " +
            "WHERE c.member.id = :memberId")
    List<CartItem> findDistinctByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_item SET quantity = quantity + :amount WHERE id = :cartItemId AND member_id = :memberId", nativeQuery = true)
    int updateQuantityByAmount(@Param("cartItemId") Long cartItemId,
                               @Param("memberId") Long memberId,
                               @Param("amount") int amount);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM cart_item WHERE member_id = :memberId AND product_detail_id = :productDetailId)", nativeQuery = true)
    Long existsCartItem(@Param("memberId") Long memberId, @Param("productDetailId") Long productDetailId);

}
