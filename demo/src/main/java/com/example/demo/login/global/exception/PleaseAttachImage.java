package com.example.demo.login.global.exception;

public class PleaseAttachImage extends IllegalArgumentException{
    public PleaseAttachImage() {
        super("사업자 이미지를 꼭 등록 해주세요");
    }
}
