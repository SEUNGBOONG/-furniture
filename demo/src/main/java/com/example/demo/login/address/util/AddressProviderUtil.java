package com.example.demo.login.address.util;

import com.example.demo.login.address.controller.dto.AddressApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AddressProviderUtil {

    @Value("${juso.api.key}")
    private String apiKey;

    @Value("${juso.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AddressApiResponse.Juso search(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String url = String.format("%s?confmKey=%s&currentPage=1&countPerPage=1&keyword=%s&resultType=json",
                    apiUrl, apiKey, encodedKeyword);

            // 문자열로 응답 받기
            String rawResponse = restTemplate.getForObject(url, String.class);
            System.out.println("💡 원본 응답: " + rawResponse);

            // JSON 구조 매핑
            ObjectMapper mapper = new ObjectMapper();
            AddressApiResponse response = mapper.readValue(rawResponse, AddressApiResponse.class);

            if (response != null && response.getResults() != null && response.getResults().getJuso() != null && !response.getResults().getJuso().isEmpty()) {
                return response.getResults().getJuso().get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("주소 검색 실패: " + e.getMessage());
        }
    }

}
