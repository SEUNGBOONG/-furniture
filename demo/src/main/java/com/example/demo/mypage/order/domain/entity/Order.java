package com.example.demo.mypage.order.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String orderId;

    private int totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> products = new ArrayList<>();

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String roadAddress;

    @Column(nullable = false)
    private String jibunAddress;

    @Column(nullable = false)
    private String zipCode;

    public void addItems(List<OrderItem> items) {
        this.products.addAll(items);
    }
}
