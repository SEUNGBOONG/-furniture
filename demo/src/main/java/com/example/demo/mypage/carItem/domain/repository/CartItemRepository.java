package com.example.demo.mypage.carItem.domain.repository;

import com.example.demo.login.member.domain.member.Member;
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

    // 특정 멤버가 특정 product(상위) 목록에 해당하는 CartItem 조회
    @Query("""
        SELECT c FROM CartItem c
        JOIN c.productDetail pd
        JOIN pd.product p
        WHERE c.member = :member AND p.id IN :productIds
    """)
    List<CartItem> findByMemberAndProductIds(@Param("member") Member member,
                                             @Param("productIds") List<Long> productIds);

    // upsert (member_id + product_detail_id 유니크 키 필요)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO cart_item (product_detail_id, member_id, quantity, price_at_added)
        VALUES (:productDetailId, :memberId, :quantity, :priceAtAdded)
        ON DUPLICATE KEY UPDATE quantity = quantity + :quantity
    """, nativeQuery = true)
    void upsertCartItem(@Param("productDetailId") Long productDetailId,
                        @Param("memberId") Long memberId,
                        @Param("quantity") int quantity,
                        @Param("priceAtAdded") int priceAtAdded);

    // 장바구니 조회 (N+1 방지용 fetch join)
    @Query("""
        SELECT DISTINCT c FROM CartItem c
        JOIN FETCH c.productDetail pd
        JOIN FETCH pd.product p
        WHERE c.member.id = :memberId
    """)
    List<CartItem> findDistinctByMemberId(@Param("memberId") Long memberId);

    // 수량 증감 (단순 증감)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE cart_item
        SET quantity = quantity + :amount
        WHERE id = :cartItemId AND member_id = :memberId
    """, nativeQuery = true)
    int updateQuantityByAmount(@Param("cartItemId") Long cartItemId,
                               @Param("memberId") Long memberId,
                               @Param("amount") int amount);

    // 수량 증감 (하한 0 보장) — 선택 사용
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE cart_item
        SET quantity = GREATEST(quantity + :amount, 0)
        WHERE id = :cartItemId AND member_id = :memberId
    """, nativeQuery = true)
    int updateQuantityByAmountFloorZero(@Param("cartItemId") Long cartItemId,
                                        @Param("memberId") Long memberId,
                                        @Param("amount") int amount);

    // 0개면 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        DELETE FROM cart_item
        WHERE id = :cartItemId AND member_id = :memberId AND quantity = 0
    """, nativeQuery = true)
    int deleteIfZero(@Param("cartItemId") Long cartItemId, @Param("memberId") Long memberId);

    // 존재 여부
    @Query(value = """
        SELECT EXISTS(
          SELECT 1 FROM cart_item
          WHERE member_id = :memberId AND product_detail_id = :productDetailId
        )
    """, nativeQuery = true)
    Long existsCartItem(@Param("memberId") Long memberId,
                        @Param("productDetailId") Long productDetailId);
}
