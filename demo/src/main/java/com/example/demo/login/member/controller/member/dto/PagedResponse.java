package com.example.demo.login.member.controller.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PagedResponse<T> {
    private long totalCount; // 전체 인원수
    private List<T> data;    // 현재 페이지 데이터
}
