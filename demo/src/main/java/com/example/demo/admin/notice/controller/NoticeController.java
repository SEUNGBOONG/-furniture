package com.example.demo.admin.notice.controller;

import com.example.demo.admin.notice.controller.dto.NoticeRequest;
import com.example.demo.admin.notice.domain.entity.Notice;
import com.example.demo.admin.notice.service.NoticeService;
import com.example.demo.login.global.annotation.Member;
import com.example.demo.product.util.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequest request, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = ProductValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        return ResponseEntity.ok(noticeService.createNotice(request));
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable Long id, @RequestBody NoticeRequest request, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = ProductValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        return ResponseEntity.ok(noticeService.updateNotice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long id, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = ProductValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }
}
