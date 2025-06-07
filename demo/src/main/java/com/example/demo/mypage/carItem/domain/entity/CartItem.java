package com.example.demo.mypage.carItem.domain.entity;

import com.example.demo.common.exception.NegativeException;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.product.domain.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int quantity;

    public void changeQuantity(int newQuantity) {
        validateNegativeQuantityException(newQuantity);
        this.quantity = newQuantity;
    }

    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }

    private static void validateNegativeQuantityException(final int newQuantity) {
        if (newQuantity < 0) {
            throw new NegativeException();
        }
    }

}
