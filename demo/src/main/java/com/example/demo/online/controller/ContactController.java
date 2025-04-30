package com.example.demo.online.controller;

import com.example.demo.online.controller.dto.ContactRequest;
import com.example.demo.online.service.ContactEmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactEmailService emailService;

    @PostMapping
    public ResponseEntity<String> submitContactForm(@RequestBody ContactRequest request) {
        try {
            emailService.sendContactEmail(request);
            return ResponseEntity.ok("문의가 성공적으로 전송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("문의 전송 중 오류가 발생했습니다.");
        }
    }
}
