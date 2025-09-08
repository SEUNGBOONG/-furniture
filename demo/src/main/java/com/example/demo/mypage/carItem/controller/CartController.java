package com.example.demo.mypage.carItem.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.login.member.exception.exceptions.MemberErrorCode;
import com.example.demo.login.member.exception.exceptions.MemberException;
import com.example.demo.mypage.carItem.controller.dto.*;
import com.example.demo.mypage.carItem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddCartRequest request,
                                          @Member Long memberId) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        cartService.addToCart(request.getProductDetailId(), request.getQuantity(), memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateQuantity(@RequestBody UpdateQuantityRequest request,
                                               @Member Long memberId) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        cartService.updateQuantityByCartItemId(
                memberId,
                request.getCartItemId(),
                request.getQuantity()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@Member Long memberId) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        cartService.clearCart(memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteSelectedItems(@Member Long memberId,
                                                    @RequestBody CartItemDeleteRequest request) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        cartService.deleteSelectedItems(memberId, request.getCartItemIds());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCart(@Member Long memberId) {
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_AUTHENTICATED);
        }
        return ResponseEntity.ok(cartService.getCart(memberId));
    }
}
