package com.example.demo.info.service;

import com.example.demo.common.exception.NotFoundCompany;
import com.example.demo.info.controller.dto.CompanyDetailDTO;
import com.example.demo.info.controller.dto.CompanyImageDTO;
import com.example.demo.info.domain.entity.CompanyInfo;
import com.example.demo.info.domain.repository.CompanyInfoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {

    private final CompanyInfoRepository repository;

    public CompanyInfoService(CompanyInfoRepository repository) {
        this.repository = repository;
    }

    public CompanyImageDTO getCompanyImageInfo() {
        CompanyInfo company = NotFoundCompanyException();
        return new CompanyImageDTO(company.getImageUrl(), company.getDescription());
    }

    @Cacheable(value = "CompanyDetailInfo")
    public CompanyDetailDTO getCompanyDetailInfo() {
        CompanyInfo company = NotFoundCompanyException();
        return new CompanyDetailDTO(
                company.getCompanyName(),
                company.getCeo(),
                company.getEstablishmentDate(),
                company.getBusinessArea(),
                company.getScale(),
                company.getMainClient()
        );
    }

    private CompanyInfo NotFoundCompanyException() {
        return repository.findFirstBy()
                .orElseThrow(NotFoundCompany::new);
    }
}
