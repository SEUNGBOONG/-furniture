package com.example.demo.payment.toss.controller;

import com.example.demo.payment.toss.controller.dto.PaymentRequestDto;
import com.example.demo.payment.toss.service.TossPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class TossPaymentController {

    private final TossPaymentService tossPaymentService;

    public TossPaymentController(TossPaymentService tossPaymentService) {
        this.tossPaymentService = tossPaymentService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPayment(@RequestBody PaymentRequestDto dto) {
        String result = tossPaymentService.requestPayment(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        return "결제 성공! paymentKey: " + paymentKey;
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam String code, @RequestParam String message) {
        return "결제 실패! code: " + code + ", message: " + message;
    }
}
