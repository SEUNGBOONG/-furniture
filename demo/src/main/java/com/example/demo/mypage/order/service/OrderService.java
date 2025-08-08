package com.example.demo.mypage.order.service;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.exception.exceptions.MemberErrorCode;
import com.example.demo.login.member.exception.exceptions.auth.NotFoundMemberId;
import com.example.demo.login.member.infrastructure.auth.JwtTokenProvider;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;
import com.example.demo.mypage.order.controller.dto.OrderRequestDTO;
import com.example.demo.mypage.order.domain.entity.Order;
import com.example.demo.mypage.order.domain.entity.OrderItem;
import com.example.demo.mypage.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

// OrderService.java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void createOrder(OrderRequestDTO dto, String token) {
        Long memberId = jwtTokenProvider.verifyToken(token).getClaim("memberId").asLong();
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberId::new);

        Order order = Order.builder()
                .orderId(dto.getOrderId())
                .totalAmount(dto.getTotalAmount())
                .orderDate(LocalDateTime.now())
                .status("PAID")
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .phoneNumber(member.getPhoneNumber())
                .roadAddress(member.getRoadAddress())
                .jibunAddress(member.getJibunAddress())
                .zipCode(member.getZipCode())
                .build();

        List<OrderItem> items = dto.getProducts().stream()
                .map(p -> OrderItem.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .size(p.getSize())
                        .unitPrice(p.getUnitPrice())
                        .quantity(p.getQuantity())
                        .totalPrice(p.getTotalPrice())
                        .order(order)
                        .build())
                .toList();

        order.addItems(items);
        orderRepository.save(order);
    }

    public Order getOrder(String orderId) {
        return orderRepository.findByOrderIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다: " + orderId));
    }

    public List<Order> getAllOrdersPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        return orderRepository.findAll(pageable).getContent();
    }
}
