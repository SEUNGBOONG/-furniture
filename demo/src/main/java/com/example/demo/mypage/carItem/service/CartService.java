package com.example.demo.mypage.carItem.service;

import com.example.demo.common.exception.CartItemNotFoundException;
import com.example.demo.login.global.exception.exceptions.NotFoundDetail;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.util.MemberValidator;
import com.example.demo.mypage.carItem.controller.dto.CartItemResponse;
import com.example.demo.mypage.carItem.controller.dto.CartSummaryResponse;
import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;
import com.example.demo.product.domain.entity.product.ProductDetail;
import com.example.demo.product.domain.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberValidator memberValidator;
    private final ProductDetailRepository productDetailRepository;

    @Transactional
    public void addToCart(Long productDetailId, int quantity, Long memberId) {
        ProductDetail detail = productDetailRepository.findById(productDetailId)
                .orElseThrow(NotFoundDetail::new);

        Member member = memberValidator.getMember(memberId);

        cartItemRepository.upsertCartItem(
                productDetailId,
                member.getId(),
                quantity,
                detail.getPrice()
        );
    }

    @Transactional
    public void increaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, 1);
        validateUpdate(updated);
    }

    @Transactional
    public void decreaseQuantity(Long memberId, Long cartItemId) {
        int updated = cartItemRepository.updateQuantityByAmount(cartItemId, memberId, -1);
        validateUpdate(updated);
        validateCartItem(cartItemId);
    }

    @Transactional
    public void updateQuantityByCartItemId(Long memberId, Long cartItemId, int newQuantity) {
        Member member = memberValidator.getMember(memberId);
        CartItem cartItem = getCartItem(cartItemId, member);
        validateUpdateQuantity(newQuantity, cartItem);
    }

    @Transactional(readOnly = true)
    public CartSummaryResponse getCart(Long memberId) {
        List<CartItem> items = cartItemRepository.findDistinctByMemberId(memberId);

        List<CartItemResponse> responses = items.stream()
                .map(CartItemResponse::fromEntity)
                .toList();

        int totalAmount = responses.stream()
                .mapToInt(CartItemResponse::getTotalPrice)
                .sum();

        return new CartSummaryResponse(responses, totalAmount);
    }

    @Transactional
    public void clearCart(Long memberId) {
        cartItemRepository.deleteByMemberId(memberId);
    }

    @Transactional
    public void deleteSelectedItems(Long memberId, List<Long> cartItemIds) {
        cartItemRepository.deleteByMemberIdAndCartItemIds(memberId, cartItemIds);
    }

    private static void validateUpdate(final int updated) {
        if (updated == 0) {
            throw new RuntimeException("수량 변경 실패");
        }
    }

    private void validateCartItem(final Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        validateQuantity(cartItem);
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
}
