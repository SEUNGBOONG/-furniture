package com.example.demo.payment.toss.util;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "toss")
public class TossPaymentProperties {
    private String clientKey;
    private String secretKey;

    public void setClientKey(String clientKey) { this.clientKey = clientKey; }

    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
}


