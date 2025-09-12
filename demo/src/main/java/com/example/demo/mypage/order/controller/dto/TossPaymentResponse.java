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
    private String status;   // e.g., "DONE"
    private String method;   // ✅ "CARD", "VIRTUAL_ACCOUNT", "EASY_PAY"

    private String approvedAt; // 결제 승인 시각 (ISO string)

    private VirtualAccount virtualAccount; // ✅ 가상계좌 정보
    private CashReceipt cashReceipt;       // ✅ 현금영수증 정보

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VirtualAccount {
        private String accountNumber; // 가상계좌 번호
        private String bankCode;      // 은행 코드 (e.g., 088 = 신한)
        private String customerName;  // 입금자명
        private String dueDate;       // 입금 만료일시 (ISO string)
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashReceipt {
        private boolean issued;       // 발급 여부
        private String type;          // "소득공제용", "지출증빙용"
        private String receiptUrl;    // 국세청 조회 URL
    }
}
