package com.example.demo.common;

public enum Setting {
    EMAIL("tmdduqflfl@naver.com"),
    AUTH_NUMBER("인증번호"),
    SUCCEED_SENDER_CERTIFICATION_NUMBER("인증 코드가 전송되었습니다."),
    SUCCEED_CERTIFICATION_NUMBER("인증 성공!"),
    FAIL_CERTIFICATION_NUMBER("인증번호가 틀립니다."),
    PLEASE_COMPLETE_EMAIL_VERIFICATION_FIRST("이메일 인증을 먼저 완료해주세요.");

    private final String setting;

    Setting(final String setting) {
        this.setting = setting;
    }

    @Override
    public String toString() {
        return setting;
    }

}
