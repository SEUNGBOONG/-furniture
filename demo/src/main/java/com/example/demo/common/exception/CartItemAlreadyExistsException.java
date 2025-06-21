package com.example.demo.common.exception;

import com.example.demo.login.global.exception.exceptions.CustomErrorCode;
import com.example.demo.login.global.exception.exceptions.CustomException;

public class CartItemAlreadyExistsException extends CustomException {

    public CartItemAlreadyExistsException() {
        super(CustomErrorCode.CART_ITEM_ALREADY_EXISTS);
    }
}
