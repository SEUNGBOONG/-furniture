package com.example.demo.info.domain.repository.company;

import com.example.demo.info.domain.entity.company.CompanyHistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyHistoryItemRepository extends JpaRepository<CompanyHistoryItem, Long> {
    List<CompanyHistoryItem> findAllByOrderByIdAsc();
}
