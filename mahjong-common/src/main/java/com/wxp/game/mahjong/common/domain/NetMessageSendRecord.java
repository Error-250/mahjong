package com.wxp.game.mahjong.common.domain;

import lombok.Data;

@Data
public class NetMessageSendRecord {
    private String messageId;
    private Integer failTimes = 0;
    private Long sendTime;
    private NetMessage message;
}
