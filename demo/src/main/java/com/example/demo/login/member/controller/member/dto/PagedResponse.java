package com.example.demo.login.member.controller.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;      // 실제 데이터
    private long totalElements;   // 전체 개수
    private int totalPages;       // 전체 페이지 수
    private int page;             // 현재 페이지
    private int size;             // 페이지 크기
}
