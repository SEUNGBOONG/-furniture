package com.example.demo.info.controller;

import com.example.demo.info.domain.entity.NewsEntity;
import com.example.demo.info.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsController {

    private final NewsService newsService;

    public NewsController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public List<NewsEntity> getNewsUrls() {
        return newsService.fetchAllNewsUrls();
    }
}
