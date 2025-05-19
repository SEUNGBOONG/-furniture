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

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberJpaRepository memberRepository;

    public void addToCart(Long productId, int quantity, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        CartItem cartItem = cartItemRepository.findByMemberAndProduct(member, product)
                .orElse(CartItem.builder()
                        .member(member)
                        .product(product)
                        .quantity(0)
                        .build());

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        List<CartItem> items = cartItemRepository.findByMemberAndProductIdIn(member, productIds);
        cartItemRepository.deleteAll(items);
    }
}
