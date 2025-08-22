package com.example.demo.admin.notice.controller;

import com.example.demo.admin.notice.controller.dto.NoticeRequest;
import com.example.demo.admin.notice.controller.dto.NoticeResponse;
import com.example.demo.admin.notice.domain.entity.Notice;
import com.example.demo.admin.notice.service.NoticeService;
import com.example.demo.login.global.annotation.Member;
import com.example.demo.common.util.AdminValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequest request, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        Notice notice = noticeService.createNotice(request);
        return ResponseEntity.ok(NoticeResponse.from(notice));
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponse>> getAllNotices() {
        var list = noticeService.getAllNotices().stream()
                .map(NoticeResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(NoticeResponse.from(noticeService.getNotice(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable Long id, @RequestBody NoticeRequest request, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        return ResponseEntity.ok(NoticeResponse.from(noticeService.updateNotice(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long id, @Member Long memberId) {
        ResponseEntity<String> FORBIDDEN = AdminValidator.getStringResponseEntity(memberId);
        if (FORBIDDEN != null) return FORBIDDEN;

        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }
}
