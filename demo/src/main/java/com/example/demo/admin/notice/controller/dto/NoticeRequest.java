package com.example.demo.admin.notice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequest {
    private String title;
    private String content;
}
