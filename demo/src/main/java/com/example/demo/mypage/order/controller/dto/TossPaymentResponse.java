package com.example.demo.mypage.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private String method;
    private String approvedAt;

    private VirtualAccount virtualAccount;
    private CashReceipt cashReceipt;
    private Card card;
    private Transfer transfer;
    private EasyPay easyPay;

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class CashReceipt {
        private boolean issued;
        private String type;
        private String receiptUrl;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Card {
        private String company;
        private String number;
        private int installmentPlanMonths;
        private boolean isInterestFree;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Transfer {
        private String bankCode;
        private String accountNumber;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class EasyPay {
        private String provider;
    }
}
