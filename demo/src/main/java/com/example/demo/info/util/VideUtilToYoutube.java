package com.example.demo.info.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VideUtilToYoutube {

    public Map<String, String> getVideoInfo() {
        Map<String, String> videoInfo = new HashMap<>();
        videoInfo.put("title", "대명 공장 제품영상");
        videoInfo.put("Date", "2020-06-27");
        videoInfo.put("videoUrl", "https://www.youtube.com/watch?v=Q4WZCZoQ6cE");
        return videoInfo;
    }

}
