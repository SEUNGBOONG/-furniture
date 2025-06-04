package com.example.demo.payment.toss.service;

import com.example.demo.payment.toss.controller.dto.PaymentRequestDto;
import com.example.demo.payment.toss.util.TossPaymentProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TossPaymentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final TossPaymentProperties tossProps;

    public TossPaymentService(TossPaymentProperties tossProps) {
        this.tossProps = tossProps;
    }

    public String requestPayment(PaymentRequestDto dto) {
        String url = "https://api.tosspayments.com/v1/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String encodedAuth = Base64.getEncoder().encodeToString((tossProps.getSecretKey() + ":").getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", dto.getAmount());
        payload.put("orderId", dto.getOrderId());
        payload.put("orderName", dto.getOrderName());
        payload.put("customerName", dto.getCustomerName());
        payload.put("successUrl", "http://localhost:8080/payment/success");
        payload.put("failUrl", "http://localhost:8080/payment/fail");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }
}
