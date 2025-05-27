package com.example.demo.info.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    public List<String> fetchAllNewsUrls() {
        return List.of(
                "https://www.newstomato.com/ReadNews.aspx?no=840909",
                "https://biz.chosun.com/site/data/html_dir/2018/08/10/2018081002371.html",
                "https://view.asiae.co.kr/news/view.htm?idxno=2018081015241195251",
                "https://www.etoday.co.kr/news/view/1651407",
                "https://n.news.naver.com/mnews/article/011/0002485389?sid=101"
        );
    }
}
