//package com.example.demo.mypage.payment.domain.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "payment")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PaymentHistory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // 주문일시
//    private LocalDateTime orderDate;
//
//    // 결제일시
//    private LocalDateTime paymentDate;
//
//    // 주문번호
//    private String orderNumber;
//
//    // 결제 상태 (예: 결제완료, 실패, 취소 등)
//    private String paymentStatus;
//
//    // 구매자명
//    private String buyerName;
//
//    // 결제 금액
//    private int amount;
//
//    // 결제 수단
//    private String paymentMethod;
//
//    // 구매 상품명
//    private String productName;
//
//    // 상점 MID
//    private String merchantId;
//
//    // 결제자 ID 또는 이메일
//    private String payer;
//
//}
