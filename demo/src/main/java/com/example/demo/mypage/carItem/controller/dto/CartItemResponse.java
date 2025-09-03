package com.example.demo.mypage.carItem.controller.dto;

import com.example.demo.mypage.carItem.domain.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private Long cartItemId;
    private Long productDetailId;   // ✅ 추가
    private Long productId;
    private String productName;
    private String size;
    private int unitPrice;
    private int quantity;
    private int totalPrice;
    private String productImage;

    public static CartItemResponse fromEntity(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductDetail().getId(),   // ✅ detailId 매핑
                item.getProductDetail().getProduct().getId(),
                item.getProductDetail().getProduct().getName(),
                item.getProductDetail().getSize(),
                item.getPriceAtAdded(),
                item.getQuantity(),
                item.getTotalPrice(),
                item.getProductDetail().getProduct().getImage()
        );
    }
}
