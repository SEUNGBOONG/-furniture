package com.example.demo.info.domain.repository.news;

import com.example.demo.info.domain.entity.news.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}
