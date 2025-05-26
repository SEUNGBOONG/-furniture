package com.example.demo.online.controller;

import com.example.demo.common.Setting;
import com.example.demo.online.controller.dto.ContactRequest;
import com.example.demo.online.util.ContactEmailUtil;

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

    private final ContactEmailUtil emailService;

    @PostMapping
    public ResponseEntity<String> submitContactForm(@RequestBody ContactRequest request) {
        try {
            emailService.sendContactEmail(request);
            return ResponseEntity.ok(Setting.CONTACT_SUCCEED_MAIL.toString());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(Setting.CONTACT_FAILED_MAIL.toString());
        }
    }
}
