package com.example.demo.info.controller;

import com.example.demo.info.util.VideUtilToYoutube;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class PromoteVideoController {

    private final VideUtilToYoutube videoInfo;

    public PromoteVideoController(final VideUtilToYoutube videoInfo) {
        this.videoInfo = videoInfo;
    }

    @GetMapping("/video-info")
    public ResponseEntity<Map<String, String>> getVideoInfo() {
        return ResponseEntity.ok(videoInfo.getVideoInfo());
    }
}
