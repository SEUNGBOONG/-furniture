package com.example.demo.mypage.payment.service;

import com.example.demo.mypage.payment.controller.dto.PaymentRequestDTO;
import com.example.demo.mypage.payment.domain.entity.PaymentHistory;
import com.example.demo.mypage.payment.domain.repository.PaymentHistoryRepository;
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

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    public void confirmPayment(PaymentRequestDTO dto) {
        // 1. 결제 정보 선 저장 (결제 요청 시각 포함)
        PaymentHistory history = paymentHistoryRepository.save(
                PaymentHistory.builder()
                        .paymentKey(dto.getPaymentKey())
                        .orderId(dto.getOrderId())
                        .amount(dto.getAmount())
                        .requestedAt(LocalDateTime.now())
                        .success(false)
                        .build()
        );

        // 2. Toss API 결제 승인 요청
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey); // ✔ Basic 인증

        Map<String, Object> requestBody = Map.of(
                "paymentKey", dto.getPaymentKey(),
                "orderId", dto.getOrderId(),
                "amount", dto.getAmount()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 3. 결제 성공 처리
                history.setSuccess(true);
                history.setApprovedAt(LocalDateTime.now()); // ✔ 결제 승인 시각 저장
                paymentHistoryRepository.save(history);
            } else {
                log.error("결제 실패 응답: {}", response.getBody());
            }

        } catch (HttpClientErrorException e) {
            log.error("Toss API 오류: {}", e.getResponseBodyAsString());

            // ✔ 실패 시에도 승인 시각 기록 (요청 응답 완료 시점 기준)
            history.setSuccess(false);
            history.setApprovedAt(LocalDateTime.now());
            paymentHistoryRepository.save(history);

            throw new RuntimeException("결제 승인 실패: " + e.getResponseBodyAsString());
        }
    }
}
