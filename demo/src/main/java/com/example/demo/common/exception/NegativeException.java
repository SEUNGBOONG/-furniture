package com.example.demo.common.exception;

import static com.example.demo.common.Setting.NEGATIVE_QUANTITY;

public class NegativeException extends IllegalArgumentException{
    public NegativeException() {
        super(String.valueOf(NEGATIVE_QUANTITY));
    }
}
