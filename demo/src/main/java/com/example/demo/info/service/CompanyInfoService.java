package com.example.demo.info.service;
import com.example.demo.info.controller.dto.CompanyDetailDTO;
import com.example.demo.info.controller.dto.CompanyImageDTO;
import com.example.demo.info.domain.entity.CompanyInfo;
import com.example.demo.info.domain.repository.CompanyInfoRepository;
import org.springframework.stereotype.Service;
@Service
public class CompanyInfoService {

    private final CompanyInfoRepository repository;

    public CompanyInfoService(CompanyInfoRepository repository) {
        this.repository = repository;
    }

    public CompanyImageDTO getCompanyImageInfo() {
        CompanyInfo company = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("회사 정보가 없습니다."));
        return new CompanyImageDTO(company.getImageUrl(), company.getDescription());
    }

    public CompanyDetailDTO getCompanyDetailInfo() {
        CompanyInfo company = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("회사 정보가 없습니다."));
        return new CompanyDetailDTO(
                company.getCompanyName(),
                company.getCeo(),
                company.getEstablishmentDate(),
                company.getBusinessArea(),
                company.getScale(),
                company.getMainClient()
        );
    }
}
