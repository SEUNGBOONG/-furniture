package com.example.demo.info.controller;

import com.example.demo.common.exception.Setting;
import com.example.demo.info.domain.MapValidator;
import com.example.demo.info.service.KakaoMapService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

    private final KakaoMapService kakaoMapService;
    private final MapValidator mapValidator;

    public MapController(KakaoMapService kakaoMapService, final MapValidator mapValidator) {
        this.kakaoMapService = kakaoMapService;
        this.mapValidator = mapValidator;
    }

    @GetMapping("/map")
    public ResponseEntity<Object> getMapCoordinates() {
        String address = Setting.ADDRESS.toString();  // 예시 주소
        String coordinates = kakaoMapService.getCoordinatesFromAddress(address);
        String[] coords = mapValidator.splitCoordinates(coordinates);
        double lat = mapValidator.parseDouble(coords[0]);
        double lon = mapValidator.parseDouble(coords[1]);

        // JSON 형식으로 반환
        return ResponseEntity.ok().body(new Coordinates(lat, lon));
    }

    public record Coordinates(double latitude, double longitude) {
    }
}
