package com.example.demo.login.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CorporationValidator {

    public static final String DATA = "data";
    public static final String B_STT = "b_stt";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${business.url}")
    private String apiUrl;

    @Value("${business.key}")
    private String serviceKey;

    public boolean isValidCorporationNumber(String businessNumber) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONArray bnoArray = new JSONArray();
            bnoArray.put(businessNumber);

            JSONObject body = new JSONObject();
            body.put("b_no", bnoArray);

            String url = apiUrl + "?serviceKey=" + serviceKey;

            HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray dataArray = jsonResponse.getJSONArray(DATA);

                if (!dataArray.isEmpty()) {
                    JSONObject businessInfo = dataArray.getJSONObject(0);
                    String status = businessInfo.getString(B_STT);

                    // "계속사업자"일 경우 true 반환
                    return "계속사업자".equals(status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}

