package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.mypage.carItem.domain.entity.CartItem;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 장바구니 전체 비우기
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CartItem c WHERE c.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    // ✅ cartItemId 기준 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CartItem c WHERE c.member.id = :memberId AND c.id IN :cartItemIds")
    void deleteByMemberIdAndCartItemIds(@Param("memberId") Long memberId,
                                        @Param("cartItemIds") List<Long> cartItemIds);

    // ✅ upsert (member_id + product_detail_id 유니크 키 필요)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO cart_item (product_detail_id, member_id, quantity, price_at_added)
        VALUES (:productDetailId, :memberId, :quantity, :priceAtAdded)
        ON DUPLICATE KEY UPDATE
            quantity = quantity + VALUES(quantity),
            price_at_added = VALUES(price_at_added)
    """, nativeQuery = true)
    void upsertCartItem(@Param("productDetailId") Long productDetailId,
                        @Param("memberId") Long memberId,
                        @Param("quantity") int quantity,
                        @Param("priceAtAdded") int priceAtAdded);

    // ✅ 수량 증감
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE cart_item
        SET quantity = quantity + :amount
        WHERE id = :cartItemId AND member_id = :memberId
    """, nativeQuery = true)
    int updateQuantityByAmount(@Param("cartItemId") Long cartItemId,
                               @Param("memberId") Long memberId,
                               @Param("amount") int amount);

    // 장바구니 조회 (fetch join)
    @Query("""
        SELECT DISTINCT c FROM CartItem c
        JOIN FETCH c.productDetail pd
        JOIN FETCH pd.product p
        WHERE c.member.id = :memberId
    """)
    List<CartItem> findDistinctByMemberId(@Param("memberId") Long memberId);
}
