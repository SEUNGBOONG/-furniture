package com.example.demo.info.domain.repository;

import com.example.demo.info.domain.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}
