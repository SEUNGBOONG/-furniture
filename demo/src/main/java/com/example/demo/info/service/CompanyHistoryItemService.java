package com.example.demo.info.service;

import com.example.demo.info.controller.dto.CompanyHistoryImageDTO;
import com.example.demo.info.controller.dto.CompanyHistoryItemDTO;
import com.example.demo.info.domain.repository.company.CompanyHistoryItemRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyHistoryItemService {

    private final CompanyHistoryItemRepository repository;

    public CompanyHistoryItemService(final CompanyHistoryItemRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "companyHistoryItems")
    public List<CompanyHistoryItemDTO> getAllHistoryItems() {
        return repository.findAllByOrderByIdAsc().stream()
                .map(item -> new CompanyHistoryItemDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getContent()
                ))
                .toList();
    }

    @Cacheable(value = "companyHistoryImage")
    public CompanyHistoryImageDTO getHistoryImageById(Long id) {
        return repository.findById(id)
                .map(item -> new CompanyHistoryImageDTO(
                        item.getImageUrl1(),
                        item.getImageUrl2()))
                .orElse(null);
    }

}
