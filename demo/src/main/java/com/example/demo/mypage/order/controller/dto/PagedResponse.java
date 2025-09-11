package com.example.demo.mypage.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;   // 현재 페이지 데이터
    private int page;          // 현재 페이지 번호
    private int size;          // 페이지 크기
    private long totalElements;// 전체 데이터 개수
    private int totalPages;    // 전체 페이지 수
}
