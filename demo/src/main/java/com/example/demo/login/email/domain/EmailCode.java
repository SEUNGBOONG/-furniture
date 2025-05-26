package com.example.demo.login.email.domain;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailCode {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder(8);
        extracted(key, random);
        return key.toString();
    }

    private static void extracted(final StringBuilder key, final Random random) {
        for (int i = 0; i < 8; i++) {
            key.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
    }
}
