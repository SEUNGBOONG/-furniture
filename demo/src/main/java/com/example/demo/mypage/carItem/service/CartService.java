package com.example.demo.mypage.carItem.service;

import com.example.demo.login.member.domain.member.Member;

import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;

import com.example.demo.mypage.carItem.domain.entity.CartItem;
import com.example.demo.mypage.carItem.domain.repository.CartItemRepository;

import com.example.demo.product.domain.entity.Product;
import com.example.demo.product.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    public static final String NOT_FOUND_MEMBER = "회원 없음";
    public static final String NOT_FOUND_PRODUCT = "상품 없음";

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberJpaRepository memberRepository;

    public void addToCart(Long productId, int quantity, Long memberId) {
        Product product = findProduct(productId);
        Member member = findMemberId(memberId);

        CartItem cartItem = findCartItemProductId(member, product);

        cartItem.increaseQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(Long memberId) {
        return cartItemRepository.findAllByMemberId(memberId);
    }

    public void clearCart(Long memberId) {
        cartItemRepository.deleteByMemberId(memberId);
    }

    public void deleteSelectedItems(Long memberId, List<Long> productIds) {
        Member member = findMemberId(memberId);

        List<CartItem> items = cartItemRepository.findByMemberAndProductIdIn(member, productIds);
        cartItemRepository.deleteAll(items);
    }

    private Member findMemberId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_MEMBER));
    }

    private Product findProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PRODUCT));
    }

    private CartItem findCartItemProductId(final Member member, final Product product) {
        return cartItemRepository.findByMemberAndProduct(member, product)
                .orElse(CartItem.builder()
                        .member(member)
                        .product(product)
                        .quantity(0)
                        .build());
    }
}
