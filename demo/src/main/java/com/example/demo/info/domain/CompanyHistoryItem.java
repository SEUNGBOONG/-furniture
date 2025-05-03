package com.example.demo.info.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "company_history_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyHistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // e.g. "1989-1999"

    @Column(length = 3000)
    private String content; // 이력 상세

    private String imageUrl1;

    private String imageUrl2;
}
