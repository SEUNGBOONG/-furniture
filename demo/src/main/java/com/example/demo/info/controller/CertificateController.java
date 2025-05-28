package com.example.demo.info.controller;

import com.example.demo.config.s3.S3Uploader;
import com.example.demo.info.domain.entity.CertificateImage;
import com.example.demo.info.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final S3Uploader s3Uploader;
    private final CertificateService certificateService;

    public CertificateController(S3Uploader s3Uploader, CertificateService certificateService) {
        this.s3Uploader = s3Uploader;
        this.certificateService = certificateService;
    }

    // 사진 업로드 (id는 서버에서 자동 생성)
    @PostMapping("/upload")
    public ResponseEntity<CertificateImage> uploadCertificate(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = s3Uploader.uploadFile(file);

        CertificateImage savedImage = certificateService.saveCertificateImage(imageUrl);

        return ResponseEntity.ok(savedImage);
    }

    @GetMapping
    public ResponseEntity<List<CertificateImage>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }
}

