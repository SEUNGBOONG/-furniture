package com.example.demo.login.email.domain;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailCode {

    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0: key.append((char) (random.nextInt(26) + 97)); break;
                case 1: key.append((char) (random.nextInt(26) + 65)); break;
                case 2: key.append(random.nextInt(10)); break;
            }
        }
        return key.toString();
    }
}
