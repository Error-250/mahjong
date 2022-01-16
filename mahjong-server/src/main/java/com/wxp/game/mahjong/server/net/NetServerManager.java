package com.wxp.game.mahjong.server.net;

import com.wxp.game.mahjong.common.net.Constant;
import com.wxp.game.mahjong.common.net.NetWorker;
import com.wxp.game.mahjong.server.util.LogWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * ServerSocket 管理者, 维护服务器建立了哪些socket
 */
public class NetServerManager extends Thread {
    // 最大连接客户端数量
    private static final int maxClientSize = 2;
    // 标识线程是否还在运行
    private boolean isRunning;
    // 当前连接了哪些socket, ip -> NetServerWorker 映射
    private Map<String, NetServerWorker> clientMap = new HashMap<>();
    // 上次客户端满员检查时间点。0表示未满
    private long clientFullFilledTime = 0;
    // server socket
    private ServerSocket serverSocket;

    protected NetServerManager() throws IOException {
        serverSocket = new ServerSocket(Constant.SERVER_PORT);
        isRunning = true;
    }

    /**
     * 线程关闭操作
     * @throws IOException 可能会有异常
     */
    public void shutdown() throws IOException {
        isRunning = false;
        for (NetServerWorker netServerWorker : clientMap.values()) {
            netServerWorker.shutdown();
        }
        clientMap.clear();
        serverSocket.close();
    }

    @Override
    public void run() {
        while (isRunning) {
            if (clientMap.size() < maxClientSize) {
                // 允许接入
                clientFullFilledTime = 0;
                try {
                    LogWorker.appendLog("Try receive client connect...");
                    Socket socket = serverSocket.accept();
                    LogWorker.appendLog(String.format("Get client %s connect", socket.getInetAddress().toString()));
                    NetServerWorker netServerWorker = new NetServerWorker(socket);
                    clientMap.put(netServerWorker.getIp(), netServerWorker);
                    netServerWorker.start();
                } catch (IOException e) {
                    LogWorker.appendLog(e.toString());
                    e.printStackTrace();
                }
            } else {
                if (clientFullFilledTime == 0) {
                    // 记录检查点
                    clientFullFilledTime = System.currentTimeMillis();
                } else {
                    // 每秒检查一次, 避免连接超时的客户端占用资源
                    if (System.currentTimeMillis() - clientFullFilledTime > 1000) {
                        // 检查是否有断线数据
                        boolean hasNoRegisterClient = false;
                        // 检查是否有客户端未注册
                        for(Map.Entry<String, NetServerWorker> entry: clientMap.entrySet()) {
                            if (!entry.getValue().isRegisterStatus()) {
                                hasNoRegisterClient = true;
                                try {
                                    entry.getValue().shutdown();
                                    entry.getValue().stop();
                                } catch (IOException e) {
                                    LogWorker.appendLog(String.format("Client %s not register, try remove. ", entry.getKey()));
                                    LogWorker.appendLog(e.toString());
                                    e.printStackTrace();
                                }
                                clientMap.remove(entry.getKey());
                            }
                        }

                        if (!hasNoRegisterClient) {
                            // 刷新检查点
                            clientFullFilledTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
    }
}
