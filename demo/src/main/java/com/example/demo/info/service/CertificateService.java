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

    public CertificateImage saveCertificateImage(String imageUrl, String tag, String description) {
        CertificateImage image = getCertificateImage(imageUrl, tag, description);
        return repository.save(image);
    }


    public List<CertificateImage> getAllCertificates() {
        return repository.findAll();
    }

    public List<CertificateImage> getCertificatesByTag(String tag) {
        return repository.findByTag(tag);
    }

    private static CertificateImage getCertificateImage(final String imageUrl, final String tag, final String description) {
        return CertificateImage.builder()
                .imageUrl(imageUrl)
                .tag(tag)
                .description(description)
                .build();
    }

}
