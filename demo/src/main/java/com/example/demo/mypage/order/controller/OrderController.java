package com.example.demo.mypage.order.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.mypage.order.controller.dto.OrderCreateRequest;
import com.example.demo.mypage.order.controller.dto.OrderResponse;
import com.example.demo.mypage.order.domain.entity.Order;
import com.example.demo.mypage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // ✅ 주문 생성 (Order 엔티티 그대로 반환 → 주문번호 확인 용도)
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateRequest request,
                                             @Member Long memberId) {
        Order order = orderService.createOrder(request, memberId);
        return ResponseEntity.ok(order);
    }

    // ✅ 단건 조회 (DTO 반환)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderService.toOrderResponse(order));
    }

    // ✅ 전체 조회 (DTO 반환)
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<Order> orders = orderService.getAllOrdersPaged(page - 1, size);
        return ResponseEntity.ok(
                orders.stream()
                        .map(orderService::toOrderResponse)
                        .toList()
        );
    }
}
