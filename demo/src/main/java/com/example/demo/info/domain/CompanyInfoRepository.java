package com.example.demo.info.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    Optional<CompanyInfo> findFirstBy(); // 가장 첫 번째 데이터 반환 (단일 회사라고 가정)
}
