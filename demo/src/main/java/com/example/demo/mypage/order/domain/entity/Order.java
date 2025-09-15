package com.example.demo.mypage.order.domain.entity;

import com.example.demo.mypage.order.controller.dto.TossPaymentResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

// com.example.demo.mypage.order.domain.entity.Order
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
    private String status; // PENDING, WAITING_FOR_DEPOSIT, DONE, CANCELED 등

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

    // ✅ Toss 결제 승인 처리 (WAITING_FOR_DEPOSIT / DONE / CANCELED 등 그대로 반영)
    public void markPaid(LocalDateTime at, TossPaymentResponse dto) {
        this.status = dto.getStatus(); // ✅ Toss 응답 status 그대로 저장
        this.paymentDate = at;
        this.paymentMethod = dto.getMethod();

        if (dto.getVirtualAccount() != null) {
            this.virtualAccountNumber = dto.getVirtualAccount().getAccountNumber();
            this.virtualBankCode = dto.getVirtualAccount().getBankCode();
            try {
                this.virtualDueDate = OffsetDateTime.parse(dto.getVirtualAccount().getDueDate()).toLocalDateTime();
            } catch (Exception e) {
                System.err.println("⚠️ dueDate 파싱 실패: " + dto.getVirtualAccount().getDueDate());
            }
        }

        if (dto.getCashReceipt() != null) {
            this.cashReceiptIssued = dto.getCashReceipt().isIssued();
        }
    }

    // ✅ Toss Webhook → 입금 완료 처리
    public void markDepositCompleted(LocalDateTime at) {
        this.status = "DONE";
        this.paymentDate = at;
    }

    // ✅ 결제 취소 처리
    public void markCanceled() {
        this.status = "CANCELED";
        this.paymentDate = null;
    }
}
