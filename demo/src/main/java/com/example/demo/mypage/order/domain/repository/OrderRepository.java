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

    List<Order> findByMemberIdOrderByOrderDateDesc(Long memberId);

    Page<Order> findByMemberId(Long memberId, Pageable pageable);

    // ✅ status 조건도 포함한 버전
    Page<Order> findByMemberIdAndStatus(Long memberId, String status, Pageable pageable);

    // ✅ 관리자용 status 조건 검색
    Page<Order> findByStatus(String status, Pageable pageable);
}
