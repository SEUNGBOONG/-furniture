package com.example.demo.mypage.carItem.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.mypage.carItem.controller.dto.CartItemIdRequest;
import com.example.demo.mypage.carItem.controller.dto.CartSummaryResponse;
import com.example.demo.mypage.carItem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartStateController {

    private final CartService cartService;

    /**
     * ✅ 장바구니 조회
     */
    @GetMapping("/state")
    public ResponseEntity<CartSummaryResponse> getCartItems(@Member Long memberId) {
        return ResponseEntity.ok(cartService.getCart(memberId));
    }

    @PostMapping("/increase")
    public ResponseEntity<Void> increaseQuantity(@RequestBody CartItemIdRequest request,
                                                 @Member Long memberId) {
        cartService.increaseQuantity(
                memberId,
                request.getCartItemId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrease")
    public ResponseEntity<Void> decreaseQuantity(@RequestBody CartItemIdRequest request,
                                                 @Member Long memberId) {
        cartService.decreaseQuantity(
                memberId,
                request.getCartItemId());
        return ResponseEntity.ok().build();
    }
}
