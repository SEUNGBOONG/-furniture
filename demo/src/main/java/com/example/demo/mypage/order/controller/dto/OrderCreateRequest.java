// com.example.demo.mypage.order.controller.dto.OrderCreateRequest.java
package com.example.demo.mypage.order.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    private List<Long> cartItemIds; // 장바구니 아이템 id 목록
}
