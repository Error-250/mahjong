package com.wxp.game.mahjong.common.domain;

import lombok.Getter;

@Getter
public enum MessageType {
    TEST(1, "test"),
    LOGIN(2, "login"),
    LOGIN_REPONSE(3, "login_resp")
    ;

    private final int code;
    private final String name;

    MessageType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
