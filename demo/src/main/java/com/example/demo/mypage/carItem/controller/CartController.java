package com.example.demo.mypage.carItem.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.mypage.carItem.controller.dto.AddCartRequest;
import com.example.demo.mypage.carItem.controller.dto.CartItemDeleteRequest;
import com.example.demo.mypage.carItem.controller.dto.CartItemResponse;
import com.example.demo.mypage.carItem.controller.dto.CartSummaryResponse;
import com.example.demo.mypage.carItem.controller.dto.UpdateQuantityRequest;
import com.example.demo.mypage.carItem.domain.Amount;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddCartRequest request,
                                          @Member Long memberId) {

        cartService.addToCart(request.getProductId(), request.getQuantity(), memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateQuantity(@RequestBody UpdateQuantityRequest request,
                                               @Member Long memberId) {
        cartService.updateQuantityByCartItemId(memberId, request.getCartItemId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCartItems(@Member Long memberId) {
        List<CartItem> cartItems = cartService.getCartItems(memberId);
        List<CartItemResponse> items = getCartItemResponses(cartItems);

        Amount totalAmount = Amount.calculateTotalAmount(items);

        return ResponseEntity.ok(new CartSummaryResponse(items, totalAmount.getTotalAmount()));
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

    private static List<CartItemResponse> getCartItemResponses(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getTotalPrice(),
                        item.getProduct().getImage()
                ))
                .collect(Collectors.toList());
    }
}
