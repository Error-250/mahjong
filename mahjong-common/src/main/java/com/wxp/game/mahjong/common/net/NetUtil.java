package com.wxp.game.mahjong.common.net;

import com.google.gson.Gson;
import com.wxp.game.mahjong.common.domain.MessageType;
import com.wxp.game.mahjong.common.domain.NetMessage;

import java.util.UUID;

public class NetUtil {
    protected static Gson gson = new Gson();

    public static NetMessage readNetMessage(String originData) {
        return gson.fromJson(originData, NetMessage.class);
    }

    public static String buildNetMessage(NetMessage netMessage) {
        return gson.toJson(netMessage);
    }

    public static NetMessage buildTestMessage() {
        NetMessage netMessage = new NetMessage();
        netMessage.setType(MessageType.TEST);
        netMessage.setMessageId(UUID.randomUUID().toString());
        netMessage.setData(null);
        return netMessage;
    }
}
