package com.example.demo.common.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException() {
        super("장바구니 아이템이 없습니다.");
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
