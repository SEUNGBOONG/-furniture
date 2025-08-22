package com.example.demo.admin.notice.controller.dto;

public record NoticeResponse(
        Long id,
        String title,
        String content
) {
    public static NoticeResponse from(com.example.demo.admin.notice.domain.entity.Notice n) {
        return new NoticeResponse(n.getId(), n.getTitle(), n.getContent());
    }
}
