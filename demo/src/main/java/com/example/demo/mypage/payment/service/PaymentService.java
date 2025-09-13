package com.example.demo.mypage.payment.service;

import com.example.demo.mypage.order.controller.dto.TossPaymentResponse;
import com.example.demo.mypage.payment.controller.dto.PaymentCancelRequestDTO;
import com.example.demo.mypage.payment.controller.dto.PaymentRequestDTO;
import com.example.demo.mypage.payment.domain.entity.PaymentHistory;
import com.example.demo.mypage.payment.domain.repository.PaymentHistoryRepository;
import com.example.demo.mypage.payment.exception.PaymentErrorCode;
import com.example.demo.mypage.payment.exception.PaymentException;
import com.example.demo.mypage.order.domain.entity.Order;
import com.example.demo.mypage.order.domain.repository.OrderRepository;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    public static final String TOSS_REDIRECT_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    /**
     * 결제 승인
     */

    @Transactional
    public TossPaymentResponse confirmPayment(PaymentRequestDTO dto) {
        PaymentHistory history = paymentHistoryRepository.save(
                PaymentHistory.builder()
                        .paymentKey(dto.getPaymentKey())
                        .orderId(dto.getOrderId())
                        .amount(dto.getAmount())
                        .requestedAt(LocalDateTime.now())
                        .success(false)
                        .build()
        );

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID));

        if (order.getTotalAmount() != dto.getAmount()) {
            throw new PaymentException(PaymentErrorCode.INVALID_AMOUNT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey, "");

        Map<String, Object> requestBody = Map.of(
                "paymentKey", dto.getPaymentKey(),
                "orderId", dto.getOrderId(),
                "amount", dto.getAmount()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TossPaymentResponse> response =
                    restTemplate.postForEntity(TOSS_REDIRECT_URL, request, TossPaymentResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                TossPaymentResponse body = response.getBody();

                history.setSuccess(true);
                history.setApprovedAt(LocalDateTime.now());
                paymentHistoryRepository.save(history);

                // ✅ 결제 승인 처리 (카드/가상계좌/간편결제)
                order.markPaid(LocalDateTime.now(), body);
                orderRepository.save(order);

                // ✅ 장바구니 비우기
                cartItemRepository.deleteByMemberId(order.getMemberId());

                return body; // 컨트롤러로 TossPaymentResponse 반환
            } else {
                throw new PaymentException(PaymentErrorCode.PAYMENT_CONFIRMATION_FAILED);
            }

        } catch (HttpClientErrorException e) {
            log.error("결제 승인 요청 실패: {}", e.getResponseBodyAsString());
            history.setApprovedAt(LocalDateTime.now());
            paymentHistoryRepository.save(history);
            throw new PaymentException(PaymentErrorCode.PAYMENT_CONFIRMATION_FAILED);
        }
    }
    // ✅ 결제 단건 조회
    public String getPaymentDetails(String paymentKey) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossSecretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("결제 조회 실패: {}", e.getResponseBodyAsString());
            throw new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID);
        }
    }

    // ✅ 주문 ID 기준 결제 조회
    public String getPaymentByOrderId(String orderId) {
        String url = "https://api.tosspayments.com/v1/payments/orders/" + orderId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossSecretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Toss 결제 조회 실패: {}", e.getResponseBodyAsString());
            throw new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID);
        }
    }

    // ✅ 결제 취소
    @Transactional
    public void cancelPayment(PaymentCancelRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey, "");

        Map<String, Object> requestBody = Map.of("cancelReason", dto.getCancelReason());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/" + dto.getPaymentKey() + "/cancel",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                PaymentHistory history = paymentHistoryRepository
                        .findByPaymentKey(dto.getPaymentKey())
                        .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID));
                history.setSuccess(false);
                history.setApprovedAt(LocalDateTime.now());
                paymentHistoryRepository.save(history);

                Order order = orderRepository.findById(history.getOrderId())
                        .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID));
                order.markCanceled();
                orderRepository.save(order);
            } else {
                throw new PaymentException(PaymentErrorCode.PAYMENT_CANCELLATION_FAILED);
            }
        } catch (HttpClientErrorException e) {
            log.error("결제 취소 요청 실패: {}", e.getResponseBodyAsString());
            throw new PaymentException(PaymentErrorCode.PAYMENT_CANCELLATION_FAILED);
        }
    }
}
