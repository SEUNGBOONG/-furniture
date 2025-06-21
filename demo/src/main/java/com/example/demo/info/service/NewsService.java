package com.example.demo.info.service;

import com.example.demo.info.domain.entity.NewsEntity;
import com.example.demo.info.domain.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(final NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsEntity> fetchAllNewsUrls() {
        return newsRepository.findAll();
    }
}
