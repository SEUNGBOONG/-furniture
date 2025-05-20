
package com.example.demo.mypage.carItem.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateQuantityRequest {
    private Long cartItemId;  // CartItem.id 로 조회
    private int quantity;
}
