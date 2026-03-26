package main.proxy;

import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;
import main.proxy.handler.impl.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final static Map<String, CommandHandler> commandHandlers = new HashMap<>();
    static {
        // User handlers
        commandHandlers.put("register", new RegisterHandler());
        commandHandlers.put("login", new LoginHandler());
        commandHandlers.put("logout", new LogoutHandler());
        commandHandlers.put("updatePassword", new UpdatePasswordHandler());
        commandHandlers.put("delete", new DeleteUserHandler());
        commandHandlers.put("search", new SearchUserHandler());
        commandHandlers.put("userList", new UserListHandler());
        commandHandlers.put("edit", new EditUserHandler());

        // Document handlers
        commandHandlers.put("info", new DocumentInfoHandler());
        commandHandlers.put("upload", new UploadHandler());
        commandHandlers.put("download", new DownloadHandler());
        commandHandlers.put("list", new ListDocumentsHandler());
        commandHandlers.put("updateFile", new UpdateFileHandler());
    }

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            Object requestObject;

            while ((requestObject = ois.readObject()) != null) {
                Request request = (Request) requestObject;
                Response response = null;

                CommandHandler handler = commandHandlers.get(request.getAction());

                if (handler != null) {
                    response = handler.execute(request);
                } else {
                    response = new Response(false, "无效的操作", null);
                }
                oos.writeObject(response);
                oos.flush();
            }
        } catch (java.io.EOFException | java.net.SocketException e) {
            System.out.println("客户端 " + clientSocket.getInetAddress().getHostAddress() + " 断开连接。");
        } catch (Exception e) {
            System.err.println("处理客户端请求时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
