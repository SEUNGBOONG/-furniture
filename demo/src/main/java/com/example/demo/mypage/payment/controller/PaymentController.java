package com.example.demo.mypage.payment.controller;

import com.example.demo.mypage.order.controller.dto.TossPaymentResponse;
import com.example.demo.mypage.payment.controller.dto.PaymentCancelRequestDTO;
import com.example.demo.mypage.payment.controller.dto.PaymentRequestDTO;
import com.example.demo.mypage.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody PaymentRequestDTO dto) {
        TossPaymentResponse result = paymentService.confirmPayment(dto);

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("status", result.getStatus());
        response.put("message", "결제 승인 처리됨");
        response.put("paymentKey", result.getPaymentKey());
        response.put("orderId", result.getOrderId());
        response.put("method", result.getMethod());

        // 가상계좌 결제일 때만 추가
        if (result.getVirtualAccount() != null) {
            response.put("virtualAccount", result.getVirtualAccount());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody TossPaymentResponse dto) {
        paymentService.handleWebhook(dto);
        return ResponseEntity.ok("✅ Webhook 처리 완료");
    }

    @GetMapping("/{paymentKey}")
    public ResponseEntity<String> getPayment(@PathVariable String paymentKey) {
        String response = paymentService.getPaymentDetails(paymentKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<String> getPaymentByOrderId(@PathVariable String orderId) {
        String result = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentCancelRequestDTO dto) {
        paymentService.cancelPayment(dto);
        return ResponseEntity.ok("결제 취소 완료");
    }
}
