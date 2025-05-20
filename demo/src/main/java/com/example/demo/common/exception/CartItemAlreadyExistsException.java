package com.example.demo.common.exception;

public class CartItemAlreadyExistsException extends RuntimeException {
    public CartItemAlreadyExistsException() {
        super("이미 장바구니에 담긴 상품입니다.");
    }

    public CartItemAlreadyExistsException(String message) {
        super(message);
    }
}
