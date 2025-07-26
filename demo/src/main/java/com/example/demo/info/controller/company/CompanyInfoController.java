package com.example.demo.info.controller.company;

import com.example.demo.info.controller.dto.CompanyDetailDTO;
import com.example.demo.info.controller.dto.CompanyImageDTO;
import com.example.demo.info.service.CompanyInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyInfoController {

    private final CompanyInfoService service;

    public CompanyInfoController(CompanyInfoService service) {
        this.service = service;
    }

    @GetMapping("/image")
    public ResponseEntity<CompanyImageDTO> getCompanyImage() {
        return ResponseEntity.ok(service.getCompanyImageInfo());
    }

    @GetMapping("/detail")
    public ResponseEntity<CompanyDetailDTO> getCompanyDetail() {
        return ResponseEntity.ok(service.getCompanyDetailInfo());
    }
}
