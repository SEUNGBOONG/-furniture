//package com.example.demo.mypage.payment.controller;
//
//import com.example.demo.mypage.payment.controller.dto.TossApproveRequest;
//import com.example.demo.mypage.payment.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/payment")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    @PostMapping("/success")
//    public ResponseEntity<?> approvePayment(@RequestBody TossApproveRequest request) {
//        return paymentService.confirmAndSavePayment(request);
//    }
//
//    @PostMapping("/fail")
//    public ResponseEntity<?> handlePaymentFailure(@RequestBody String errorMessage) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 실패: " + errorMessage);
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<?> getPaymentDetails(@PathVariable String orderId) {
//        return paymentService.getPaymentByOrderId(orderId);
//    }
//
//    @PostMapping("/cancel")
//    public ResponseEntity<?> cancelPayment(@RequestBody TossApproveRequest request) {
//        return paymentService.cancelPayment(request);
//    }
//
//}
