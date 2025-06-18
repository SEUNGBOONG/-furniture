package com.example.demo.mypage.carItem.domain;

import com.example.demo.common.exception.CartItemAlreadyExistsException;
import com.example.demo.common.exception.CartItemNotFoundException;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;
import org.springframework.stereotype.Component;

@Component
public class CartValidator {

    private final CartItemRepository cartItemRepository;

    public CartValidator(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public static void validateUpdate(final int updated) {
        if (updated == 0) {
            throw new RuntimeException();
        }
    }

    public void validateCartItem(final Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        validateQuantity(cartItem);
    }

    private void validateQuantity(final CartItem cartItem) {
        if (cartItem.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
        }
    }

    public void validateUpdateQuantity(final int newQuantity, final CartItem cartItem) {
        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.changeQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    public static void validateExists(final Long exists) {
        if (exists != null && exists == 1L) {
            throw new CartItemAlreadyExistsException();
        }
    }
}
