package com.example.demo.mypage.carItem.service;

import com.example.demo.common.exception.CartItemNotFoundException;
import com.example.demo.login.member.domain.member.Member;

import com.example.demo.login.member.domain.member.MemberValidator;

import com.example.demo.mypage.carItem.domain.CartValidator;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberValidator memberValidator;
    private final CartValidator cartValidator;

    public void addToCart(Long productId, int quantity, Long memberId) {
        Long exists = cartItemRepository.existsCartItem(memberId, productId);
        CartValidator.validateExists(exists);
        cartItemRepository.upsertCartItem(productId, memberId, quantity);
    }

    public void increaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, 1);
        CartValidator.validateUpdate(updated);
    }

    public void decreaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, -1);
        CartValidator.validateUpdate(updated);
        cartValidator.validateCartItem(cartItemId);
    }

    public void updateQuantityByCartItemId(Long memberId, Long cartItemId, int newQuantity) {
        Member member = memberValidator.getMember(memberId);
        CartItem cartItem = getCartItem(cartItemId, member);
        cartValidator.validateUpdateQuantity(newQuantity, cartItem);
    }

    public List<CartItem> getCartItems(Long memberId) {
        return cartItemRepository.findDistinctByMemberId(memberId);
    }

    public void clearCart(Long memberId) {
        cartItemRepository.deleteByMemberId(memberId);
    }

    public void deleteSelectedItems(Long memberId, List<Long> productIds) {
        Member member = memberValidator.getMember(memberId);

        List<CartItem> items = cartItemRepository.findByMemberAndProductIdIn(member, productIds);
        cartItemRepository.deleteAll(items);
    }

    private CartItem getCartItem(final Long cartItemId, final Member member) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        if (!cartItem.getMember().equals(member)) {
            throw new CartItemNotFoundException();
        }
        return cartItem;
    }
}
