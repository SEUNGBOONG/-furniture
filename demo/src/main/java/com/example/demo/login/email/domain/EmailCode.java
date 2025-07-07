package com.example.demo.login.email.domain;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailCode {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String createCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 범위의 6자리 정수
        return String.valueOf(code); // 여전히 String으로 반환 (비교, 보관 편리)
    }

}
