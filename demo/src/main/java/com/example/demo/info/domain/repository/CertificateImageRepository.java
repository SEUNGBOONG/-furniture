package com.example.demo.info.domain.repository;

import com.example.demo.info.domain.entity.CertificateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateImageRepository extends JpaRepository<CertificateImage, Long> {
    List<CertificateImage> findByTag(String tag);
}
