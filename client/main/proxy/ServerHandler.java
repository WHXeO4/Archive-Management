package main.proxy;

import main.entity.Request;
import main.entity.Response;

import java.io.*;
import java.net.Socket;

public class ServerHandler{
    private static Socket client;
    private static OutputStream outToServer;
    private static ObjectOutputStream oos;
    private static InputStream inFromServer;
    private static ObjectInputStream ois;

    /**
     * 初始化请求所需的对象
     * @param clientSocket 客户端的socket
     */
    public static void setClient(Socket clientSocket) throws IOException {
        client = clientSocket;

        outToServer = client.getOutputStream();
        oos = new ObjectOutputStream(outToServer);

        inFromServer = client.getInputStream();
        ois = new ObjectInputStream(inFromServer);
    }

    /**
     * 向后端发送请求
     * @param action 请求行为的枚举值
     * @param data 请求携带的数据
     * @return 响应数据
     */
    synchronized public static Response run(String action, Object data) throws ClassNotFoundException, IOException {
        Request request = new Request(action, data);

        oos.writeObject(request);
        oos.flush();

        Response response = (Response) ois.readObject();

        return response;
    }
}
