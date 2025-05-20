package com.example.demo.common.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException() {
        super("상품 없음");
    }

    public NotFoundProductException(String message) {
        super(message);
    }
}
