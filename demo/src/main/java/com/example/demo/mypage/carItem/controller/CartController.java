package com.example.demo.mypage.carItem.controller;

import com.example.demo.login.global.annotation.Member;

import com.example.demo.mypage.carItem.controller.dto.AddCartRequest;
import com.example.demo.mypage.carItem.controller.dto.CartItemDeleteRequest;
import com.example.demo.mypage.carItem.controller.dto.UpdateQuantityRequest;

import com.example.demo.mypage.carItem.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddCartRequest request,
                                          @Member Long memberId) {
        putInAShoppingCart(request, memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateQuantity(@RequestBody UpdateQuantityRequest request,
                                               @Member Long memberId) {
        cartService.updateQuantityByCartItemId(memberId,
                request.getCartItemId(),
                request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@Member Long memberId) {
        cartService.clearCart(memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteSelectedItems(@Member Long memberId,
                                                    @RequestBody CartItemDeleteRequest request) {
        cartService.deleteSelectedItems(memberId, request.getProductIds());
        return ResponseEntity.ok().build();
    }

    private void putInAShoppingCart(final AddCartRequest request, final Long memberId) {
        cartService.addToCart(
                request.getProductId(),
                request.getQuantity(),
                memberId);
    }
}
