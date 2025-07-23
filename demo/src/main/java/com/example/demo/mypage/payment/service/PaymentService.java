package com.example.demo.mypage.payment.service;

import com.example.demo.mypage.payment.controller.dto.TossApproveRequest;
import com.example.demo.mypage.payment.domain.entity.PaymentHistory;
import com.example.demo.mypage.payment.domain.repository.PaymentHistoryRepository;
import com.example.demo.mypage.payment.exception.PaymentErrorCode;
import com.example.demo.mypage.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    public ResponseEntity<?> confirmAndSavePayment(TossApproveRequest request) {

        String url = "https://api.tosspayments.com/v1/payments/confirm";

        // 요청 바디 구성
        JSONObject body = new JSONObject();
        body.put("paymentKey", request.getPaymentKey());
        body.put("orderId", request.getOrderId());
        body.put("amount", request.getAmount());

        // 인증 헤더 구성 (Basic Auth)
        String encodedAuth = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, httpEntity, String.class);

            JSONObject json = new JSONObject(response.getBody());

            PaymentHistory payment = PaymentHistory.builder()
                    .orderDate(LocalDateTime.now())
                    .paymentDate(LocalDateTime.now())
                    .orderNumber(json.getString("orderId"))
                    .paymentStatus(json.getString("status"))
                    .buyerName(json.optJSONObject("card") != null ? json.getJSONObject("card").optString("owner", "") : "")
                    .amount(json.getInt("totalAmount"))
                    .paymentMethod(json.getString("method"))
                    .productName(json.getString("orderName"))
                    .merchantId(json.getString("mId"))
                    .payer(json.optJSONObject("customer") != null ? json.getJSONObject("customer").optString("email", "") : "")
                    .build();

            paymentHistoryRepository.save(payment);

            return ResponseEntity.ok("결제 승인 및 저장 완료");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 승인 실패: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getPaymentByOrderId(String orderId) {
        PaymentHistory history = paymentHistoryRepository.findByOrderNumber(orderId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_BY_ORDER_ID));

        return ResponseEntity.ok(history);
    }

    public ResponseEntity<?> cancelPayment(TossApproveRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/" + request.getPaymentKey() + "/cancel";

        JSONObject body = new JSONObject();
        body.put("cancelReason", "사용자 요청 취소");

        String encodedAuth = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, httpEntity, String.class);

            JSONObject json = new JSONObject(response.getBody());
            String orderId = json.getString("orderId");

            paymentHistoryRepository.findByOrderNumber(orderId).ifPresent(payment -> {
                payment.setPaymentStatus("CANCELED");
                paymentHistoryRepository.save(payment);
            });

            return ResponseEntity.ok("결제 취소 완료");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 취소 실패: " + e.getMessage());
        }
    }
}
