package com.example.demo.mypage.order.domain.entity;

import com.example.demo.mypage.order.controller.dto.TossPaymentResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private int totalAmount;
    private boolean shipped;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private String status;

    private String paymentMethod;        // 카드 / 가상계좌 / 간편결제
    private String virtualAccountNumber; // 가상계좌 번호
    private String virtualBankCode;      // 은행 코드
    private LocalDateTime virtualDueDate;// 입금 기한
    private boolean cashReceiptIssued;   // 현금영수증 발급 여부

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> products = new ArrayList<>();

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String roadAddress;

    @Column(nullable = false)
    private String jibunAddress;

    @Column(nullable = false)
    private String zipCode;

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public void addItems(List<OrderItem> items) {
        for (OrderItem i : items) {
            i.setOrder(this);
        }
        this.products.addAll(items);
    }

    // ✅ 결제 승인 처리 (카드/가상계좌/현금영수증 포함)
// Order.java
    public void markPaid(LocalDateTime at, TossPaymentResponse dto) {
        this.status = "PAID";
        this.paymentDate = at;
        this.paymentMethod = dto.getMethod(); // 공통 필드

        // ✅ 가상계좌
        if (dto.getVirtualAccount() != null) {
            this.virtualAccountNumber = dto.getVirtualAccount().getAccountNumber();
            this.virtualBankCode = dto.getVirtualAccount().getBankCode();
            this.virtualDueDate = LocalDateTime.parse(dto.getVirtualAccount().getDueDate());
        }

        // ✅ 현금영수증
        if (dto.getCashReceipt() != null) {
            this.cashReceiptIssued = dto.getCashReceipt().isIssued();
        }

        // ✅ 카드
        if (dto.getCard() != null) {
            this.paymentMethod = "CARD"; // 카드 결제라고 명시
        }

        // ✅ 계좌이체
        if (dto.getTransfer() != null) {
            this.paymentMethod = "TRANSFER"; // 계좌이체라고 명시
        }

        // ✅ 간편결제
        if (dto.getEasyPay() != null) {
            this.paymentMethod = "EASY_PAY"; // 간편결제
        }
    }

    // ✅ 결제 취소 처리
    public void markCanceled() {
        this.status = "CANCELED";
        this.paymentDate = null;
    }
}
