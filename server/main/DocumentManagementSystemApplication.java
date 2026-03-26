package main;

import main.proxy.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DocumentManagementSystemApplication {
    // 定义服务器监听的端口号
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, IllegalAccessException {
        app();
    }

    public static void app() {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            serverSocket.setSoTimeout(10000);
            System.out.println("服务器已启动，正在监听端口: " + PORT);


            while (true) {

                Socket clientSocket = serverSocket.accept();
//                clientSocket.setSoTimeout(600000);
                System.out.println("接收到新的客户端连接: " + clientSocket.getInetAddress().getHostAddress());

                 threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("服务器运行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

