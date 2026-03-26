package main;

import main.view.WelcomeView;
import main.proxy.ServerHandler;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class DocumentManagementClient {
    private final static int PORT = 8080;
    private static Socket client;
    private static boolean isConnected = false;

    public static void main(String[] args) {
        startConnectionListener();
        app();
    }

    static void app() {
        SwingUtilities.invokeLater(() -> {
            WelcomeView welcomeView = new WelcomeView();
            welcomeView.setVisible(true);
        });
    }

    private static void startConnectionListener() {
        Thread connectionThread = new Thread(() -> {
            int cnt = 1;
            while (true) {
                if (client == null || client.isClosed()) {
                    isConnected = false;
                    System.out.println("正在尝试连接... 次数(" + cnt + ")");
                    try {
                        client = new Socket("localhost", PORT);
                        ServerHandler.setClient(client);
                        isConnected = true;
                        System.out.println("连接成功");
                        cnt = 1;
                    } catch (IOException e) {
                        System.out.println("连接失败: " + e.getMessage());
                        client = null;
                        cnt++;
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        });
        connectionThread.setDaemon(true);
        connectionThread.start();
    }
}
