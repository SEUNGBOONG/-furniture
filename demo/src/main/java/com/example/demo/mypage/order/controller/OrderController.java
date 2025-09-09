package com.example.demo.mypage.order.controller;

import com.example.demo.common.util.AdminValidator;
import com.example.demo.login.global.annotation.Member;
import com.example.demo.login.member.exception.exceptions.MemberErrorCode;
import com.example.demo.login.member.exception.exceptions.MemberException;
import com.example.demo.mypage.order.controller.dto.OrderCreateRequest;
import com.example.demo.mypage.order.controller.dto.OrderResponse;
import com.example.demo.mypage.order.domain.entity.Order;
import com.example.demo.mypage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // ✅ 내 주문 조회 (로그인된 사용자 본인 것만)
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@Member Long memberId) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        return ResponseEntity.ok(
                orderService.getOrdersByMember(memberId).stream()
                        .map(orderService::toOrderResponse)
                        .toList()
        );
    }

    // ✅ 관리자 전용 주문 전체 조회
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return (ResponseEntity) FORBIDDEN;

        return ResponseEntity.ok(
                orderService.getAllOrdersPaged(0, 50).stream()
                        .map(orderService::toOrderResponse)
                        .toList()
        );
    }

    @PutMapping("/admin/{orderId}/ship")
    public ResponseEntity<Map<String, Object>> updateOrderShipped(@PathVariable String orderId,
                                                                  @RequestParam boolean shipped,
                                                                  @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return (ResponseEntity) FORBIDDEN;

        orderService.updateShippedStatus(orderId, shipped);
        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "shipped", shipped,
                "message", shipped ? "배송 시작으로 저장되었습니다." : "배송 미시작으로 저장되었습니다."
        ));
    }

    // ✅ 단건 조회 (DTO 반환)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderService.toOrderResponse(order));
    }
}
