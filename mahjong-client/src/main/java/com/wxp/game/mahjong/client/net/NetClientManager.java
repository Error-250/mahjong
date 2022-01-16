package com.wxp.game.mahjong.client.net;

import com.wxp.game.mahjong.common.domain.NetMessage;
import com.wxp.game.mahjong.common.domain.NetMessageSendRecord;
import com.wxp.game.mahjong.common.net.Constant;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 维护一个socket, 实现消息重试机制。
 */
public class NetClientManager extends Thread {
    public static NetClientWorker netClientWorker;
    private static Map<String, NetMessageSendRecord> messageFailTimesMap = new HashMap<>();
    private static NetClientManager instance;
    private static boolean isRunning = false;

    private NetClientManager () {
        isRunning = true;
    }

    public static void initClientNet() throws IOException {
        if (netClientWorker == null) {
            Socket socket = new Socket(InetAddress.getLocalHost(), Constant.SERVER_PORT);
            try {
                netClientWorker = new NetClientWorker(socket);
                netClientWorker.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (instance == null) {
            instance = new NetClientManager();
        }
        instance.start();
    }

    public static void shutdown() {
        isRunning = false;
        if (netClientWorker != null) {
            try {
                netClientWorker.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
            netClientWorker.stop();
            netClientWorker = null;
        }
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }

    public static void recordMessage(NetMessage netMessage) {
        NetMessageSendRecord netMessageSendRecord = new NetMessageSendRecord();
        netMessageSendRecord.setMessage(netMessage);
        netMessageSendRecord.setSendTime(System.currentTimeMillis());
        netMessageSendRecord.setMessageId(netMessageSendRecord.getMessageId());

        messageFailTimesMap.put(netMessageSendRecord.getMessageId(), netMessageSendRecord);
    }

    public static void confirmMessage(String messageId) {
        messageFailTimesMap.remove(messageId);
    }

    @Override
    public void run() {
        while (isRunning) {
            // 检查是不是有数据需要重试
            long currentTime = System.currentTimeMillis();
            messageFailTimesMap.values().stream()
                    .filter(netMessageSendRecord -> currentTime - netMessageSendRecord.getSendTime() > 1000)
                    .forEach(netMessageSendRecord -> {
                        // 记录失败次数
                        netMessageSendRecord.setFailTimes(netMessageSendRecord.getFailTimes() + 1);

                        // 重试
                        netClientWorker.sendMessage(NetClientUtil.buildNetMessage(netMessageSendRecord.getMessage()));

                        // 记录时间
                        netMessageSendRecord.setSendTime(System.currentTimeMillis());
                    });

            try {
                // 每秒处理一次
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void doLogin() {
        String clientId = UUID.randomUUID().toString();
        netClientWorker.sendLoginMessage(clientId);
        System.out.println("send message");
    }
}
