package com.wxp.game.mahjong.server.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogWorker extends Thread {
    private static StringProperty logProperty = new SimpleStringProperty();
    private static StringBuilder allLog = new StringBuilder();
    private static final int MAX_QUEUE_SIZE = 100;
    private static List<String> logQueue = new ArrayList<String>(MAX_QUEUE_SIZE);
    private static LogWorker instance = null;
    private static boolean isRunning = false;

    private LogWorker() {
        super("LogWorker");
        isRunning = true;
    }

    public static void init() {
        if (instance == null) {
            instance = new LogWorker();
        }
        instance.start();
    }

    public static StringProperty getLogProperty() {
        return logProperty;
    }

    public static void appendLog(String log) {
        synchronized (logQueue) {
            if (logQueue.size() < MAX_QUEUE_SIZE) {
                logQueue.add(log);
                logQueue.notify();
            }
        }
    }

    public static void shutdown() {
        isRunning = false;
        if (instance != null) {
            instance.stop();
        }
        instance = null;
    }

    @Override
    public void run() {
        while (isRunning) {
            synchronized (logQueue) {
                if (logQueue.size() > 0) {
                    // 需要处理日志
                    Iterator<String> iterator = logQueue.iterator();

                    while (iterator.hasNext()) {
                        String currentLog = iterator.next();
                        allLog.append(currentLog);
                        allLog.append("\n");
                        iterator.remove();
                    }

                    logProperty.setValue(allLog.toString());
                } else {
                    try {
                        logQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
