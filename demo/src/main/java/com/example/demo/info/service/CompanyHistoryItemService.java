package com.example.demo.info.service;

import com.example.demo.info.controller.dto.CompanyHistoryImageDTO;
import com.example.demo.info.controller.dto.CompanyHistoryItemDTO;
import com.example.demo.info.domain.repository.CompanyHistoryItemRepository;
import org.springframework.cache.annotation.CacheEvict;
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

    // 여기 @Cacheable 추가해서 Redis에 캐시 생성되도록 해야 합니다.
    @Cacheable(value = "companyHistoryImage")
    public CompanyHistoryImageDTO getHistoryImageById(Long id) {
        return repository.findById(id)
                .map(item -> new CompanyHistoryImageDTO(
                        item.getImageUrl1(),
                        item.getImageUrl2()))
                .orElse(null);
    }

    @CacheEvict(value = "companyHistoryImage", key = "1")  // id=1 캐시 삭제
    public void evictImageCache() {
        // 캐시 삭제용 빈 메서드
    }
}
