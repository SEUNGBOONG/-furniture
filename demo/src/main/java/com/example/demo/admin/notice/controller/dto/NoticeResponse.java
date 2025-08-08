package com.example.demo.admin.notice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
}
