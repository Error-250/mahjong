package com.wxp.game.mahjong.client.net;

import com.wxp.game.mahjong.common.domain.LoginMessage;
import com.wxp.game.mahjong.common.domain.MessageType;
import com.wxp.game.mahjong.common.domain.NetMessage;
import com.wxp.game.mahjong.common.net.NetUtil;

import java.util.UUID;

public class NetClientUtil extends NetUtil {
    public static NetMessage buildLoginMessage(String clientId) {
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setClientId(clientId);

        NetMessage netMessage = new NetMessage();
        netMessage.setMessageId(UUID.randomUUID().toString());
        netMessage.setType(MessageType.LOGIN);
        netMessage.setData(gson.toJson(loginMessage));

        return netMessage;
    }
}
