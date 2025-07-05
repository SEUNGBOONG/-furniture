package com.example.demo.common.exception;

public class NotFoundCompany extends RuntimeException{
    public NotFoundCompany() {
        super(String.valueOf(Setting.NOT_FOUND_COMPANY));
    }
}
