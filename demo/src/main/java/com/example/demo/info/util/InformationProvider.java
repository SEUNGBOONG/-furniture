package com.example.demo.info.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InformationProvider {

    public Map<String, String> getVideoInfo() {
        Map<String, String> videoInfo = new HashMap<>();
        videoInfo.put("title", "대명 공장 제품영상");
        videoInfo.put("Date", "2020-06-27");
        videoInfo.put("videoUrl", "https://www.youtube.com/watch?v=Q4WZCZoQ6cE");
        return videoInfo;
    }

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
