package com.example.demo.common.exception;

public class CartItemAccessDeniedException extends RuntimeException {
    public CartItemAccessDeniedException() {
        super("해당 회원의 장바구니 아이템이 아닙니다.");
    }

    public CartItemAccessDeniedException(String message) {
        super(message);
    }
}
