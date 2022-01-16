package com.wxp.game.mahjong.common.domain;

import lombok.Data;

@Data
public class NetMessage {
    private String messageId;
    private MessageType type;
    private String data;
}
