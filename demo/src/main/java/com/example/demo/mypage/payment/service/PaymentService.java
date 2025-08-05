package com.example.demo.mypage.payment.service;

import com.example.demo.mypage.payment.controller.dto.PaymentCancelRequestDTO;
import com.example.demo.mypage.payment.controller.dto.PaymentRequestDTO;
import com.example.demo.mypage.payment.domain.entity.PaymentHistory;
import com.example.demo.mypage.payment.domain.repository.PaymentHistoryRepository;
import com.example.demo.mypage.payment.exception.PaymentErrorCode;
import com.example.demo.mypage.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    public static final String TOSS_REDIRECT_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    public void confirmPayment(PaymentRequestDTO dto) {
        // 1. 결제 정보 선 저장
        PaymentHistory history = paymentHistoryRepository.save(
                PaymentHistory.builder()
                        .paymentKey(dto.getPaymentKey())
                        .orderId(dto.getOrderId())
                        .amount(dto.getAmount())
                        .requestedAt(LocalDateTime.now())
                        .success(false)
                        .build()
        );

        // 2. Toss 결제 승인 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey, ""); // Base64 Encoding 자동 처리

        Map<String, Object> requestBody = Map.of(
                "paymentKey", dto.getPaymentKey(),
                "orderId", dto.getOrderId(),
                "amount", dto.getAmount()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(TOSS_REDIRECT_URL, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                history.setSuccess(true);
                history.setApprovedAt(LocalDateTime.now());
                paymentHistoryRepository.save(history);
            } else {
                log.error("결제 실패 응답: {}", response.getBody());
                throw new PaymentException(PaymentErrorCode.PAYMENT_CONFIRMATION_FAILED);
            }

        } catch (HttpClientErrorException e) {

            history.setSuccess(false);
            history.setApprovedAt(LocalDateTime.now());
            paymentHistoryRepository.save(history);

            throw new PaymentException(PaymentErrorCode.PAYMENT_CONFIRMATION_FAILED);
        }
    }

    public String getPaymentDetails(String paymentKey) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossSecretKey, "");  // Toss Secret Key로 인증
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody(); // 혹은 DTO로 매핑해도 OK
        } catch (HttpClientErrorException e) {
            log.error("결제 조회 실패: {}", e.getResponseBodyAsString());
            throw new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID);
        }
    }

    public String getPaymentByOrderId(String orderId) {
        String url = "https://api.tosspayments.com/v1/payments/orders/" + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossSecretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Toss 결제 조회 실패: {}", e.getResponseBodyAsString());
            throw new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID);
        }
    }
    // PaymentService.java
    public void cancelPayment(PaymentCancelRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey, "");

        Map<String, Object> requestBody = Map.of(
                "cancelReason", dto.getCancelReason()
        );

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
                history.setApprovedAt(LocalDateTime.now()); // 취소 처리일로 갱신
                paymentHistoryRepository.save(history);
            } else {
                throw new PaymentException(PaymentErrorCode.PAYMENT_CANCELLATION_FAILED);
            }

        } catch (HttpClientErrorException e) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_CANCELLATION_FAILED);
        }
    }
}
