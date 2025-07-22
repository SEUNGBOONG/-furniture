package com.example.demo.mypage.carItem.service;

import com.example.demo.common.exception.CartItemAlreadyExistsException;
import com.example.demo.common.exception.CartItemNotFoundException;
import com.example.demo.login.member.domain.member.Member;

import com.example.demo.login.util.MemberValidator;

import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;

import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.domain.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberValidator memberValidator;
    private final ProductDetailRepository productDetailRepository;

    public void addToCart(Long productDetailId, int quantity, Long memberId) {
        ProductDetail detail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("상세 정보 없음"));

        Member member = memberValidator.getMember(memberId);

        cartItemRepository.upsertCartItem(
                productDetailId,
                member.getId(),
                quantity,
                detail.getPrice()
        );
    }

    public void increaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, 1);
        validateUpdate(updated);
    }

    public void decreaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, -1);
        validateUpdate(updated);
        validateCartItem(cartItemId);
    }

    private void validateCartItem(final Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        validateQuantity(cartItem);
    }

    public void updateQuantityByCartItemId(Long memberId, Long cartItemId, int newQuantity) {
        Member member = memberValidator.getMember(memberId);
        CartItem cartItem = getCartItem(cartItemId, member);
        validateUpdateQuantity(newQuantity, cartItem);
    }

    public List<CartItem> getCartItems(Long memberId) {
        return cartItemRepository.findDistinctByMemberId(memberId);
    }

    public void clearCart(Long memberId) {
        cartItemRepository.deleteByMemberId(memberId);
    }

    public void deleteSelectedItems(Long memberId, List<Long> productIds) {
        Member member = memberValidator.getMember(memberId);

        List<CartItem> items = cartItemRepository.findByMemberAndProductIds(member, productIds);
        cartItemRepository.deleteAll(items);
    }

    private static void validateUpdate(final int updated) {
        if (updated == 0) {
            throw new RuntimeException();
        }
    }

    private void validateQuantity(final CartItem cartItem) {
        if (cartItem.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
        }
    }

    private void validateUpdateQuantity(final int newQuantity, final CartItem cartItem) {
        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.changeQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    private CartItem getCartItem(final Long cartItemId, final Member member) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        if (!cartItem.getMember().equals(member)) {
            throw new CartItemNotFoundException();
        }
        return cartItem;
    }

    private static void validateExists(final Long exists) {
        if (exists != null && exists == 1L) {
            throw new CartItemAlreadyExistsException();
        }
    }
}
