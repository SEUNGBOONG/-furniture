package com.example.demo.common.exception;

import com.example.demo.login.global.exception.exceptions.CustomErrorCode;
import com.example.demo.login.global.exception.exceptions.CustomException;

public class CartItemNotFoundException extends CustomException {
    public CartItemNotFoundException() {
        super(CustomErrorCode.CART_NOT_FOUND_ITEM);
    }
}
