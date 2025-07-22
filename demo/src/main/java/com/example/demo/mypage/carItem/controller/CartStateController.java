package com.example.demo.mypage.carItem.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.mypage.carItem.controller.dto.CartItemIdRequest;
import com.example.demo.mypage.carItem.controller.dto.CartItemResponse;
import com.example.demo.mypage.carItem.controller.dto.CartSummaryResponse;
import com.example.demo.mypage.carItem.domain.Amount;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartStateController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCartItems(@Member Long memberId) {
        List<CartItem> cartItems = cartService.getCartItems(memberId);
        List<CartItemResponse> items = getCartItemResponses(cartItems);
        Amount totalAmount = Amount.calculateTotalAmount(items);
        return ResponseEntity.ok(new CartSummaryResponse(items, totalAmount.getTotalAmount()));
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

    private static List<CartItemResponse> getCartItemResponses(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
