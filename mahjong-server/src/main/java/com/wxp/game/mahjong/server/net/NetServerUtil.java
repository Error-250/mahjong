package com.wxp.game.mahjong.server.net;

import com.wxp.game.mahjong.common.domain.LoginMessage;
import com.wxp.game.mahjong.common.domain.MessageType;
import com.wxp.game.mahjong.common.domain.NetMessage;
import com.wxp.game.mahjong.common.net.NetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetServerUtil extends NetUtil {
    private static NetServerManager netServerManager;
    private static Map<String, NetServerWorker> clientMap = new HashMap<>();

    public static void initNet() throws IOException {
        if (netServerManager == null) {
            netServerManager = new NetServerManager();
        }
        netServerManager.start();
    }

    public static void stopNet() throws IOException {
        if (netServerManager != null) {
            netServerManager.shutdown();
            netServerManager = null;
        }
    }

    public static void registerClient(String clientId, NetServerWorker netServerWorker) {
        clientMap.put(clientId, netServerWorker);
    }

    public static void unregisterClient(String clientId) {
        clientMap.remove(clientId);
    }

    public static LoginMessage parseLoginMessage(NetMessage netMessage) {
        if (MessageType.LOGIN.equals(netMessage.getType())) {
            return gson.fromJson(netMessage.getData(), LoginMessage.class);
        }
        return null;
    }

    public static String buildServerResp(String messageId, MessageType messageType) {
        NetMessage netMessage = new NetMessage();
        netMessage.setMessageId(messageId);
        netMessage.setType(messageType);
        netMessage.setData(null);
        return buildNetMessage(netMessage);
    }
}
