package com.example.demo.common.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("회원 없음");
    }
}
