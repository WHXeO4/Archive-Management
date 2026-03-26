package main.controller;

import main.entity.Response;
import main.proxy.ServerHandler;

import javax.swing.*;

public abstract class AbstractController {
    protected Response response;

    /**
     * 像后端发送请求并获取响应数据
     * @param params 要向后端发送的数据集合
     * @return 响应数据
     * @throws InterruptedException
     */
    public abstract Response apply(Object... params) throws InterruptedException;

    /**
     * 判断请求行为是否成功
     * @param response 响应数据
     * @return true-请求成功；false-请求失败
     */
     protected boolean isSuccess(Response response) {
        if (response==null) return false;

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "成功", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, response.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 生成一个 thread 来向后端发送请求，防止线程阻塞
     * @param action 请求行为的枚举值
     * @param object 请求携带的数据
     * @return 线程对象
     */
    protected Thread getThread(String action, Object object) {
        return new Thread(()->{
            try {
                response = ServerHandler.run(action, object);
            } catch (Exception e) {
                e.printStackTrace();
                response = new Response(false, "Error", null);
            }
        });
    }
}
