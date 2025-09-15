package com.example.demo.mypage.order.domain.repository;

import com.example.demo.mypage.order.domain.entity.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o JOIN FETCH o.products WHERE o.orderId = :orderId")
    Optional<Order> findByOrderIdWithItems(@Param("orderId") String orderId);

    // ✅ 회원별 주문 전체 조회 (최신순)
    List<Order> findByMemberIdOrderByOrderDateDesc(Long memberId);
    // 기존 단순 List 메소드 대신 페이징 지원 메소드 추가
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
