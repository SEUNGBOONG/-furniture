package com.example.demo.info.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class PromoteVideoController {

    @GetMapping("/video-info")
    public ResponseEntity<Map<String, String>> getVideoInfo() {
        Map<String, String> videoInfo = new HashMap<>();
        videoInfo.put("title", "대명 공장 제품영상");
        videoInfo.put("Date", "2020-06-27");
        videoInfo.put("videoUrl", "https://www.youtube.com/watch?v=Q4WZCZoQ6cE");
        return ResponseEntity.ok(videoInfo);
    }
}
