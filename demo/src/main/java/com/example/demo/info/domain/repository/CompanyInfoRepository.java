package com.example.demo.info.domain.repository;

import com.example.demo.info.domain.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    Optional<CompanyInfo> findFirstBy();
}
