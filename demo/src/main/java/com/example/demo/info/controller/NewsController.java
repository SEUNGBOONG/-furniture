package com.example.demo.info.controller;

import com.example.demo.info.util.InformationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsController {

    private final InformationProvider informationProvider;

    public NewsController(final InformationProvider informationProvider) {
        this.informationProvider = informationProvider;
    }

    @GetMapping("/news")
    public List<String> getNewsUrls() {
        return informationProvider.fetchAllNewsUrls();
    }
}
