package com.wxp.game.mahjong.server.ui;

import lombok.Getter;

@Getter
public enum ServerUIEnum {
    DEFAULT(1, "default"),
    SERVER_ON_SERVICE(2, "server_on_service")
    ;

    private final int code;
    private final String name;

    ServerUIEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
