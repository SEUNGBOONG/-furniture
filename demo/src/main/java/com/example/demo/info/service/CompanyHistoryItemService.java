package com.example.demo.info.service;

import com.example.demo.info.controller.dto.CompanyHistoryItemDTO;
import com.example.demo.info.domain.repository.CompanyHistoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyHistoryItemService {

    private final CompanyHistoryItemRepository repository;

    public CompanyHistoryItemService(CompanyHistoryItemRepository repository) {
        this.repository = repository;
    }

    public List<CompanyHistoryItemDTO> getAllHistoryItems() {
        return repository.findAllByOrderByIdAsc().stream()
                .map(item -> new CompanyHistoryItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getContent(),
                        item.getImageUrl1(),
                        item.getImageUrl2()))
                .toList();
    }
}

