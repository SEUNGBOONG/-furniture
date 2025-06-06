package com.example.demo.info.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class KakaoMapService {

    private final RestTemplate restTemplate;

    @Value("${kakao.client.id}")
    private String apiKey;

    public KakaoMapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCoordinatesFromAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        // API 호출을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);  // 'KakaoAK' 뒤에 API 키 추가

        // HTTP 요청 보내기
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

        // 응답에서 위도와 경도 추출 (JSON 파싱)
        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONObject firstResult = jsonResponse.getJSONArray("documents").getJSONObject(0);

        double lat = firstResult.getDouble("y");  // 위도
        double lon = firstResult.getDouble("x");  // 경도

        return lat + "," + lon;  // "위도,경도" 형식으로 반환
    }
}
