package com.example.demo.mypage.payment.domain.repository;

import com.example.demo.mypage.payment.domain.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    Optional<PaymentHistory> findByOrderNumber(String orderNumber);
}
