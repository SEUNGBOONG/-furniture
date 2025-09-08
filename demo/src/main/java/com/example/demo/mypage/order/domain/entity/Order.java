package com.example.demo.mypage.order.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime orderDate;

    private LocalDateTime paymentDate;   // ✅ 결제 완료 시점

    @Column(nullable = false)
    private String status;               // ✅ 주문 상태 (PENDING, PAID, CANCELED 등)

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

    // 그대로 boolean shipped 필드 유지
    private boolean shipped;

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    // OrderItem 연결
    public void addItems(List<OrderItem> items) {
        for (OrderItem i : items) {
            i.setOrder(this);
        }
        this.products.addAll(items);
    }

    // ✅ 결제 승인 처리
    public void markPaid(LocalDateTime at) {
        this.status = "PAID";
        this.paymentDate = at;
    }

    // ✅ 결제 취소 처리
    public void markCanceled() {
        this.status = "CANCELED";
        this.paymentDate = null;
    }
}
