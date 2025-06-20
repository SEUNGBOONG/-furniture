package com.example.demo.info.domain.repository;

import com.example.demo.info.domain.entity.CompanyHistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyHistoryItemRepository extends JpaRepository<CompanyHistoryItem, Long> {
    List<CompanyHistoryItem> findAllByOrderByIdAsc();
    List<CompanyHistoryItem> findAllByOrderById();
}
