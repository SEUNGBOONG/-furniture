package com.example.demo.mypage.order.service;

import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.exception.exceptions.auth.NotFoundMemberId;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;
import com.example.demo.mypage.notification.util.OrderCompletedEvent;
import com.example.demo.mypage.order.controller.dto.OrderCreateRequest;
import com.example.demo.mypage.order.controller.dto.OrderItemResponse;
import com.example.demo.mypage.order.controller.dto.OrderResponse;
import com.example.demo.mypage.order.controller.dto.PagedResponse;
import com.example.demo.mypage.order.domain.entity.Order;
import com.example.demo.mypage.order.domain.entity.OrderItem;
import com.example.demo.mypage.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ApplicationEventPublisher eventPublisher; // ✅ 이벤트 발행기 추가

    /**
     * 주문 생성
     */
    @Transactional
    public Order createOrder(OrderCreateRequest request, Long memberId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberId::new);

        // 장바구니 아이템 조회
        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());

        // 총액 계산
        int totalAmount = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        // 주문 생성 (PENDING 상태)
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .totalAmount(totalAmount)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .shipped(false)
                .memberId(member.getId())
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .phoneNumber(member.getPhoneNumber())
                .roadAddress(member.getRoadAddress())
                .jibunAddress(member.getJibunAddress())
                .zipCode(member.getZipCode())
                .build();

        // OrderItem 생성 (CartItem → OrderItem 스냅샷)
        List<OrderItem> items = cartItems.stream()
                .map(ci -> OrderItem.builder()
                        .productId(ci.getProductDetail().getProduct().getId().toString())
                        .productName(ci.getProductDetail().getProduct().getName())
                        .size(ci.getProductDetail().getSize())
                        .unitPrice(ci.getPriceAtAdded())
                        .quantity(ci.getQuantity())
                        .totalPrice(ci.getTotalPrice())
                        .order(order)
                        .build())
                .toList();

        order.addItems(items);

        // ✅ DB 저장
        Order savedOrder = orderRepository.save(order);

        // ✅ 주문 완료 이벤트 발행
        eventPublisher.publishEvent(new OrderCompletedEvent(savedOrder));

        return savedOrder;
    }

    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public Order getOrder(String orderId) {
        return orderRepository.findByOrderIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다: " + orderId));
    }

    /**
     * 관리자 주문 전체 조회 (페이징 + status 필터)
     */
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getAllOrdersPaged(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orders;
        if (status != null && !status.isBlank()) {
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        List<OrderResponse> content = orders.getContent().stream()
                .map(this::toOrderResponse)
                .toList();

        return new PagedResponse<>(
                content,
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );
    }

    /**
     * ✅ Order → OrderResponse 변환
     */
    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getProducts().stream()
                .map(i -> new OrderItemResponse(
                        i.getProductId(),
                        i.getProductName(),
                        i.getSize(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getTotalPrice(),
                        null
                ))
                .toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                order.getPaymentDate(),
                order.getMemberName(),
                order.getPhoneNumber(),
                order.getRoadAddress(),
                order.getJibunAddress(),
                order.getZipCode(),
                order.isShipped(),
                order.getPaymentMethod(),
                order.getVirtualAccountNumber(),
                order.getVirtualBankCode(),
                order.getVirtualDueDate(),
                order.isCashReceiptIssued(),
                items
        );
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByMember(Long memberId) {
        return orderRepository.findByMemberIdOrderByOrderDateDesc(memberId);
    }

    @Transactional
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다: " + orderId));
        orderRepository.delete(order); // ✅ Cascade 로 OrderItem 같이 삭제됨
    }

    /**
     * 내 주문 조회 (페이징 + status 필터)
     */
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getOrdersByMemberPaged(Long memberId, int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orders;
        if (status != null && !status.isBlank()) {
            orders = orderRepository.findByMemberIdAndStatus(memberId, status, pageable);
        } else {
            orders = orderRepository.findByMemberId(memberId, pageable);
        }

        List<OrderResponse> content = orders.getContent().stream()
                .map(this::toOrderResponse)
                .toList();

        return new PagedResponse<>(
                content,
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );
    }

    @Transactional
    public void updateShippedStatus(String orderId, boolean shipped) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문이 없습니다: " + orderId));
        order.setShipped(shipped); // 저장 버튼 누른 값으로 반영
        orderRepository.save(order);
    }
}
