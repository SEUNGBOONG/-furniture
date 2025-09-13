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
    // ✅ 결제 승인 처리 (카드/가상계좌/현금영수증 포함)
    public void markPaid(LocalDateTime at, TossPaymentResponse dto) {
        this.status = "PAID";
        this.paymentDate = at;
        this.paymentMethod = dto.getMethod();

        // 가상계좌 처리
        if (dto.getVirtualAccount() != null) {
            this.virtualAccountNumber = dto.getVirtualAccount().getAccountNumber();
            this.virtualBankCode = dto.getVirtualAccount().getBankCode();

            try {
                this.virtualDueDate = OffsetDateTime.parse(dto.getVirtualAccount().getDueDate()).toLocalDateTime();
            } catch (Exception e) {
                System.err.println("⚠️ 가상계좌 dueDate 파싱 실패: " + dto.getVirtualAccount().getDueDate());
            }
        }

        // 현금영수증 처리
        if (dto.getCashReceipt() != null) {
            this.cashReceiptIssued = dto.getCashReceipt().isIssued();
        }
    }

    // ✅ 결제 취소 처리
    public void markCanceled() {
        this.status = "CANCELED";
        this.paymentDate = null;
    }
}
