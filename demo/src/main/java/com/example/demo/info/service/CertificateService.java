package com.example.demo.info.service;

import com.example.demo.info.domain.entity.CertificateImage;
import com.example.demo.info.domain.repository.CertificateImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    private final CertificateImageRepository repository;

    public CertificateService(CertificateImageRepository repository) {
        this.repository = repository;
    }

    public CertificateImage saveCertificateImage(String imageUrl) {
        CertificateImage img = new CertificateImage();
        img.setImageUrl(imageUrl);
        return repository.save(img);
    }

    public List<CertificateImage> getAllCertificates() {
        return repository.findAll();
    }
}
