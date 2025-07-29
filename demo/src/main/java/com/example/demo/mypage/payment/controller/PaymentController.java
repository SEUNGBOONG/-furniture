package com.example.demo.mypage.payment.controller;

import com.example.demo.mypage.payment.controller.dto.PaymentCancelRequestDTO;
import com.example.demo.mypage.payment.controller.dto.PaymentRequestDTO;
import com.example.demo.mypage.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequestDTO dto) {
        paymentService.confirmPayment(dto);
        return ResponseEntity.ok("결제 승인 처리됨");
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
