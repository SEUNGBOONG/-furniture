package com.example.demo.info.controller;

import com.example.demo.info.domain.MapValidator;
import com.example.demo.info.service.KakaoMapService;

import lombok.Getter;
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
        String address = "경기도 화성시 장안면 돌서지길 132-46";  // 예시 주소
        String coordinates = kakaoMapService.getCoordinatesFromAddress(address);
        // "위도,경도" 문자열을 쉼표로 분리하여 JSON 객체로 반환
        String[] coords = mapValidator.splitCoordinates(coordinates);
        double lat = Double.parseDouble(coords[0]);
        double lon = Double.parseDouble(coords[1]);

        // JSON 형식으로 반환
        return ResponseEntity.ok().body(new Coordinates(lat, lon));
    }

    // Coordinates DTO 클래스
    @Getter
    public record Coordinates(double latitude, double longitude) {
    }
}
